/*
 *    Copyright 2016 Information Control Company
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.icct.ucd.DeployComponent
import com.icct.ucd.UrbanCodeConfiguration

def call(UrbanCodeConfiguration config, DeployComponent component, String applicationName, String version)
{
	echo "Publishing component version to Urban Code Deploy: ${component.name}."
	
    step([$class: 'UCDeployPublisher',
		siteName: config.site,
		component: 
		[
			$class: 'com.urbancode.jenkins.plugins.ucdeploy.VersionHelper$VersionBlock',
			componentName: component.name,
			createComponent: 
			[
				$class: 'com.urbancode.jenkins.plugins.ucdeploy.ComponentHelper$CreateComponentBlock',
				componentTemplate: '',
				componentApplication: applicationName
			],
			delivery: 
			[
				$class: 'com.urbancode.jenkins.plugins.ucdeploy.DeliveryHelper$Push',
				pushVersion: version,
				baseDir: component.artifactBasePath,
				fileIncludePatterns: component.artifactSpec,
				fileExcludePatterns: '',
				pushProperties: 'jenkins.server=Local\njenkins.reviewed=false',
				pushDescription: 'Pushed from Jenkins',
				pushIncremental: false
			]
		]
	])
	
	withEnv(["PATH+UDCLIENT=${tool 'UrbanCodeClient'}"])
	{
		withCredentials([usernamePassword(credentialsId: config.credential, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')])
		{
			sh "udclient -username ${USERNAME} -password ${PASSWORD} -weburl ${config.url} addVersionStatus -component ${component.name} -version ${version} -status Stable"
		}
	}
}
