/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.pulsar.broker.systopic;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.common.events.EventType;
import org.apache.pulsar.common.events.EventsTopicNames;
import org.apache.pulsar.common.naming.NamespaceName;
import org.apache.pulsar.common.naming.TopicName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamespaceEventsSystemTopicFactory {

    private final PulsarClient client;

    public NamespaceEventsSystemTopicFactory(PulsarClient client) {
        this.client = client;
    }

    public SystemTopicClient createSystemTopic(NamespaceName namespaceName, EventType eventType) {
        TopicName topicName = getSystemTopicName(namespaceName, eventType);
        if (topicName != null) {
            log.info("Create system topic {} for {}", topicName.toString(), eventType);
            return new TopicPoliciesSystemTopicClient(client, topicName);
        } else {
            return null;
        }
    }

    public static TopicName getSystemTopicName(NamespaceName namespaceName, EventType eventType) {
        switch (eventType) {
            case TOPIC_POLICY:
                return TopicName.get("persistent", namespaceName,
                        EventsTopicNames.NAMESPACE_EVENTS_LOCAL_NAME);
            default:
                return null;
        }
    }

    private static final Logger log = LoggerFactory.getLogger(NamespaceEventsSystemTopicFactory.class);
}
