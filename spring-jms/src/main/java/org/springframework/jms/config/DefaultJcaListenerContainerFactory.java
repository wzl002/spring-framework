/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.jms.config;

import javax.resource.spi.ResourceAdapter;

import org.springframework.jms.listener.endpoint.JmsActivationSpecConfig;
import org.springframework.jms.listener.endpoint.JmsActivationSpecFactory;
import org.springframework.jms.listener.endpoint.JmsMessageEndpointManager;
import org.springframework.jms.support.destination.DestinationResolver;

/**
 * A {@link JmsListenerContainerFactory} implementation to build a
 * JCA {@link JmsMessageEndpointManager}.
 *
 * @author Stephane Nicoll
 * @since 4.1
 */
public class DefaultJcaListenerContainerFactory implements JmsListenerContainerFactory<JmsMessageEndpointManager> {

	private ResourceAdapter resourceAdapter;

	private JmsActivationSpecFactory activationSpecFactory;

	private DestinationResolver destinationResolver;

	private Object transactionManager;

	private JmsActivationSpecConfig activationSpecConfig;

	/**
	 * @see JmsMessageEndpointManager#setResourceAdapter(ResourceAdapter)
	 */
	public void setResourceAdapter(ResourceAdapter resourceAdapter) {
		this.resourceAdapter = resourceAdapter;
	}

	/**
	 * @see JmsMessageEndpointManager#setActivationSpecFactory(JmsActivationSpecFactory)
	 */
	public void setActivationSpecFactory(JmsActivationSpecFactory activationSpecFactory) {
		this.activationSpecFactory = activationSpecFactory;
	}

	/**
	 * @see JmsMessageEndpointManager#setDestinationResolver(DestinationResolver)
	 */
	public void setDestinationResolver(DestinationResolver destinationResolver) {
		this.destinationResolver = destinationResolver;
	}

	/**
	 * @see JmsMessageEndpointManager#setTransactionManager(Object)
	 */
	public void setTransactionManager(Object transactionManager) {
		this.transactionManager = transactionManager;
	}

	/**
	 * @see JmsMessageEndpointManager#setActivationSpecConfig(JmsActivationSpecConfig)
	 */
	public void setActivationSpecConfig(JmsActivationSpecConfig activationSpecConfig) {
		this.activationSpecConfig = activationSpecConfig;
	}

	/**
	 * Return the current {@link JmsActivationSpecConfig}.
	 */
	public JmsActivationSpecConfig getActivationSpecConfig() {
		return activationSpecConfig;
	}

	/**
	 * Create an empty container instance.
	 */
	protected JmsMessageEndpointManager createContainerInstance() {
		return new JmsMessageEndpointManager();
	}

	@Override
	public JmsMessageEndpointManager createMessageListenerContainer(JmsListenerEndpoint endpoint) {
		if (this.destinationResolver != null && this.activationSpecFactory != null) {
			throw new IllegalStateException("Specify either 'activationSpecFactory' or " +
					"'destinationResolver', not both. If you define a dedicated JmsActivationSpecFactory bean, " +
					"specify the custom DestinationResolver there (if possible)");
		}

		JmsMessageEndpointManager instance = createContainerInstance();

		if (this.resourceAdapter != null) {
			instance.setResourceAdapter(this.resourceAdapter);
		}
		if (this.activationSpecFactory != null) {
			instance.setActivationSpecFactory(this.activationSpecFactory);
		}
		if (this.destinationResolver != null) {
			instance.setDestinationResolver(this.destinationResolver);
		}
		if (this.transactionManager != null) {
			instance.setTransactionManager(this.transactionManager);
		}
		if (this.activationSpecConfig != null) {
			instance.setActivationSpecConfig(this.activationSpecConfig);
		}

		endpoint.setupMessageContainer(instance);

		return instance;
	}

}