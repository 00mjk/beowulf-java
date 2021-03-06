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
package com.beowulfchain.beowulfj.plugins.apis.account.history.models.deserializer;

import com.beowulfchain.beowulfj.communication.CommunicationHandler;
import com.beowulfchain.beowulfj.plugins.apis.account.history.models.AppliedOperation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.joou.UInteger;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Due to Issue #1152 of the FasterXML/jackson-databind project, it is not
 * possible to parse Maps in an Array Format with Jackson out of the box.
 * Therefore this custom deserializer is required.
 */
public class AppliedOperationHashMapDeserializer extends JsonDeserializer<Map<UInteger, AppliedOperation>> {
    @Override
    public Map<UInteger, AppliedOperation> deserialize(JsonParser jsonParser,
                                                       DeserializationContext deserializationContext) throws IOException {

        HashMap<UInteger, AppliedOperation> result = new HashMap<>();

        ObjectCodec codec = jsonParser.getCodec();
        TreeNode rootNode = codec.readTree(jsonParser);

        if (rootNode.isArray()) {
            for (JsonNode node : (ArrayNode) rootNode) {
                result.put(UInteger.valueOf(node.get(0).asLong()),
                        CommunicationHandler.getObjectMapper().treeToValue(node.get(1), AppliedOperation.class));
            }

            return result;
        }

        throw new IllegalArgumentException("JSON Node is not an array.");
    }
}
