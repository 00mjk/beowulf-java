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
package com.beowulfchain.beowulfj.protocol.operations.virtual;

import com.beowulfchain.beowulfj.enums.PrivateKeyType;
import com.beowulfchain.beowulfj.enums.ValidationType;
import com.beowulfchain.beowulfj.exceptions.BeowulfInvalidTransactionException;
import com.beowulfchain.beowulfj.interfaces.SignatureObject;
import com.beowulfchain.beowulfj.protocol.AccountName;
import com.beowulfchain.beowulfj.protocol.Asset;
import com.beowulfchain.beowulfj.protocol.operations.Operation;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

/**
 * This class represents the Beowulf "fill_vesting_withdraw_operation" object.
 */
public class FillVestingWithdrawOperation extends Operation {
    @JsonProperty("from_account")
    private AccountName fromAccount;
    @JsonProperty("to_account")
    private AccountName toAccount;
    private Asset withdrawn;
    private Asset deposited;

    /**
     * This operation is a virtual one and can only be created by the blockchain
     * itself. Due to that, this constructor is private.
     */
    private FillVestingWithdrawOperation() {
        super(true);
    }

    /**
     * @return the fromAccount
     */
    public AccountName getFromAccount() {
        return fromAccount;
    }

    /**
     * @return the toAccount
     */
    public AccountName getToAccount() {
        return toAccount;
    }

    /**
     * @return the withdrawn
     */
    public Asset getWithdrawn() {
        return withdrawn;
    }

    /**
     * @return the deposited
     */
    public Asset getDeposited() {
        return deposited;
    }

    @Override
    public byte[] toByteArray() throws BeowulfInvalidTransactionException {
        // The byte representation is not needed for virtual operations as we
        // can't broadcast them.
        return new byte[0];
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public Map<SignatureObject, PrivateKeyType> getRequiredAuthorities(
            Map<SignatureObject, PrivateKeyType> requiredAuthoritiesBase) {
        // A virtual operation can't be created by the user, therefore it also
        // does not require any authority.
        return null;
    }

    @Override
    public void validate(ValidationType validationType) {
        // There is no need to validate virtual operations.
    }
}
