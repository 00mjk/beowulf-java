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
package com.beowulfchain.beowulfj.base.models;

import com.beowulfchain.beowulfj.exceptions.BeowulfInvalidTransactionException;
import com.beowulfchain.beowulfj.interfaces.ByteTransformable;
import eu.bittrade.crypto.core.CryptoUtils;
import org.joou.UInteger;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class Ripemd160 implements ByteTransformable, Serializable {
    /**
     * Generated serial uid.
     */
    private static final long serialVersionUID = 7984783145088522082L;
    /**
     * Contains the ripemd160.
     */
    private byte[] hashValue;

    /**
     * Create a new wrapper for the given ripemd160 hash.
     *
     * @param hashValue The hash to wrap.
     */
    public Ripemd160(String hashValue) {
        this.setHashValue(hashValue);
    }

    /**
     * Convert the first four bytes of the hash into a number.
     *
     * @return The number.
     */
    public int getNumberFromHash() {
        byte[] fourBytesByte = new byte[4];
        System.arraycopy(hashValue, 0, fourBytesByte, 0, 4);
        return ByteBuffer.wrap(fourBytesByte).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    /**
     * Get the wrapped hash value in its long representation.
     *
     * @return The wrapped hash value in its long representation.
     */
    public UInteger getHashValue() {
        return UInteger.valueOf(CryptoUtils.readUint32(hashValue, 4));
    }

    /**
     * Set the hash value by providing its decoded byte representation.
     *
     * @param hashValue The hash to wrap.
     */
    public void setHashValue(byte[] hashValue) {
        this.hashValue = hashValue;
    }

    /**
     * Set the hash value by providing its encoded String representation.
     *
     * @param hashValue The hash to wrap.
     */
    public void setHashValue(String hashValue) {
        this.hashValue = CryptoUtils.HEX.decode(hashValue);
    }

    @Override
    public byte[] toByteArray() throws BeowulfInvalidTransactionException {
        return this.hashValue;
    }

    @Override
    public String toString() {
        return CryptoUtils.HEX.encode(this.hashValue);
    }
}
