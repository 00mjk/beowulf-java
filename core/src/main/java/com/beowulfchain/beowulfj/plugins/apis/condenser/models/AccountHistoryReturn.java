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
package com.beowulfchain.beowulfj.plugins.apis.condenser.models;

import com.beowulfchain.beowulfj.plugins.apis.account.history.models.AppliedOperation;
import com.beowulfchain.beowulfj.plugins.apis.condenser.models.deserializer.AccountHistoryDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.joou.UInteger;

@JsonDeserialize(using = AccountHistoryDeserializer.class)
public class AccountHistoryReturn {
    private UInteger sequence;
    private AppliedOperation appliedOperation;

    public UInteger getSequence() {
        return sequence;
    }

    public void setSequence(UInteger sequence) {
        this.sequence = sequence;
    }

    public AppliedOperation getAppliedOperation() {
        return appliedOperation;
    }

    public void setAppliedOperation(AppliedOperation appliedOperation) {
        this.appliedOperation = appliedOperation;
    }
}
