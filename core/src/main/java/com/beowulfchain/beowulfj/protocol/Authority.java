/*
 *     This file is part of BeowulfJ (formerly known as 'Beowulf-Java-Api-Wrapper')
 *
 *     BeowulfJ is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     BeowulfJ is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.beowulfchain.beowulfj.protocol;

import com.beowulfchain.beowulfj.base.models.deserializer.AccountAuthHashMapDeserializer;
import com.beowulfchain.beowulfj.base.models.deserializer.PublicKeyHashMapDeserializer;
import com.beowulfchain.beowulfj.base.models.serializer.AccountAuthHashMapSerializer;
import com.beowulfchain.beowulfj.base.models.serializer.PublicKeyHashMapSerializer;
import com.beowulfchain.beowulfj.exceptions.BeowulfInvalidParamException;
import com.beowulfchain.beowulfj.exceptions.BeowulfInvalidTransactionException;
import com.beowulfchain.beowulfj.interfaces.ByteTransformable;
import com.beowulfchain.beowulfj.interfaces.SignatureObject;
import com.beowulfchain.beowulfj.util.BeowulfJUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;
import java.util.*;

/**
 * This class is the java implementation of the Beowulf "authority" object.
 */
public class Authority implements ByteTransformable, SignatureObject {
    // Type is uint32 in the original code.
    @JsonProperty("weight_threshold")
    private long weightThreshold;
    /*
     * In the original code the type is "account_authority_map" which looks like
     * this: <p> flat_map< account_name_type, weight_type, string_less > </p>
     */
    @JsonSerialize(using = AccountAuthHashMapSerializer.class)
    @JsonDeserialize(using = AccountAuthHashMapDeserializer.class)
    @JsonProperty("account_auths")
    private Map<AccountName, Integer> accountAuths;
    /*
     * In the original code the type is "key_authority_map" which looks like
     * this: <p> flat_map< public_key_type, weight_type > </p>
     */
    @JsonSerialize(using = PublicKeyHashMapSerializer.class)
    @JsonDeserialize(using = PublicKeyHashMapDeserializer.class)
    @JsonProperty("key_auths")
    private Map<PublicKey, Integer> keyAuths;

    /**
     * Constructor thats set required values to avoid null pointer exceptions.
     */
    public Authority() {
        this.setAccountAuths(new HashMap<AccountName, Integer>());
        this.setKeyAuths(new HashMap<PublicKey, Integer>());
        // Set default values.
        this.setWeightThreshold(1L);
    }

    /**
     * @return the weightThreshold
     */
    public long getWeightThreshold() {
        return weightThreshold;
    }

    /**
     * @param weightThreshold The weight threshold.
     */
    public void setWeightThreshold(long weightThreshold) {
        if (weightThreshold < 1L) {
            throw new BeowulfInvalidParamException("Weight threshold must not lower than 1");
        }
        this.weightThreshold = weightThreshold;
    }

    /**
     * @return A map of stored account names and their threshold.
     */
    public Map<AccountName, Integer> getAccountAuths() {
        return accountAuths;
    }

    /**
     * @param accountAuths The account auths.
     */
    public void setAccountAuths(Map<AccountName, Integer> accountAuths) {
        this.accountAuths = accountAuths;
    }

    /**
     * @return A map of stored public keys and their threshold.
     */
    public Map<PublicKey, Integer> getKeyAuths() {
        return keyAuths;
    }

    /**
     * @param keyAuths Map key auths.
     */
    public void setKeyAuths(Map<PublicKey, Integer> keyAuths) {
        this.keyAuths = keyAuths;
    }

    /**
     * Check if the authority is impossible.
     *
     * @return <code>true</code> if the authority is impossible, otherwise
     * <code>false</code>.
     */
    @JsonIgnore
    public boolean isImpossible() {
        long authWeights = 0;
        for (int weight : this.getAccountAuths().values()) {
            authWeights += weight;
        }
        for (int weight : this.getKeyAuths().values()) {
            authWeights += weight;
        }

        return authWeights < this.getWeightThreshold();
    }

    @Override
    public byte[] toByteArray() throws BeowulfInvalidTransactionException {
        try (ByteArrayOutputStream serializedAuthority = new ByteArrayOutputStream()) {
            serializedAuthority.write(BeowulfJUtils.transformIntToByteArray((int) this.getWeightThreshold()));

            serializedAuthority.write(BeowulfJUtils.transformLongToVarIntByteArray(this.getAccountAuths().size()));

            // get sorted keyset by alphabet
            List<AccountName> accountAuthsKeySet = new ArrayList<>(this.getAccountAuths().keySet());
            accountAuthsKeySet.sort(Comparator.comparing(AccountName::getName));

            for (AccountName accountName : accountAuthsKeySet) {
                serializedAuthority.write(accountName.toByteArray());
                serializedAuthority.write(BeowulfJUtils.transformShortToByteArray(this.getAccountAuths().get(accountName)));
            }

            serializedAuthority.write(BeowulfJUtils.transformLongToVarIntByteArray(this.getKeyAuths().size()));

            // get sorted keyset by alphabet
            List<PublicKey> keyAuthsKeySet = new ArrayList<>(this.getKeyAuths().keySet());
            keyAuthsKeySet.sort(Comparator.comparing(PublicKey::getAddressFromPublicKey));
            for (PublicKey key : keyAuthsKeySet) {
                serializedAuthority.write(key.toByteArray());
                serializedAuthority.write(BeowulfJUtils.transformShortToByteArray(this.getKeyAuths().get(key)));
            }

            return serializedAuthority.toByteArray();
        } catch (IOException e) {
            throw new BeowulfInvalidTransactionException(
                    "A problem occured while transforming an asset into a byte array.", e);
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object otherAuthority) {
        if (this == otherAuthority)
            return true;
        if (otherAuthority == null || !(otherAuthority instanceof Authority))
            return false;
        Authority other = (Authority) otherAuthority;
        return this.getAccountAuths().equals(other.getAccountAuths()) && this.getKeyAuths().equals(other.getKeyAuths())
                && this.getWeightThreshold() == other.getWeightThreshold();
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (this.getAccountAuths() == null ? 0 : this.getAccountAuths().hashCode());
        hashCode = 31 * hashCode + (this.getKeyAuths() == null ? 0 : this.getKeyAuths().hashCode());
        hashCode = 31 * hashCode + (int) (this.getWeightThreshold() ^ (this.getWeightThreshold() >>> 32));
        return hashCode;
    }

    /**
     * Returns {@code true} if, and only if, the account name has more than
     * {@code 0} characters.
     *
     * @return {@code true} if the account name has more than {@code 0},
     * otherwise {@code false}
     */
    @JsonIgnore
    public boolean isEmpty() {
        return this.getAccountAuths().isEmpty() && this.getKeyAuths().isEmpty();
    }
}
