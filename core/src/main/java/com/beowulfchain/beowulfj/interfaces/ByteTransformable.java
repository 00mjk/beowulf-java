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
package com.beowulfchain.beowulfj.interfaces;

import com.beowulfchain.beowulfj.exceptions.BeowulfInvalidTransactionException;

/**
 * This interface is used to make sure each operation implements a method to get
 * its byte representation.
 */
public interface ByteTransformable {
    /**
     * Covert the operation into a byte array.
     *
     * @return The operation as a byte array.
     * @throws BeowulfInvalidTransactionException If there was a problem while transforming the transaction
     *                                            into a byte array.
     */
    byte[] toByteArray() throws BeowulfInvalidTransactionException;
}
