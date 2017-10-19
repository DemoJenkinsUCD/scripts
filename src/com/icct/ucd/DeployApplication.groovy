package com.genco.commerce.build;

import java.io.Serializable

class DeployApplication implements Serializable
{
	String name
	String process
	Map components
	
	DeployApplication(String name, String process)
	{
		this.name = name
		this.process = process
	}
	
	def addComponentVersion(DeployComponent component, String version)
	{
		components.put(component, version)
	}
	
	String componentSpecification()
	{
		def componentList = components.collect {key, value -> key.name + ':' + value}
		componentList.join('\n')
	}
}
