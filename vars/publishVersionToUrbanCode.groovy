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

def call(String componentName, String applicationName, String version, String baseDir)
{
	echo "Publishing component version to Urban Code Deploy: ${componentName}."
	
    step([$class: 'UCDeployPublisher',
		siteName: "${UCD_SITE}",
		component: 
		[
			$class: 'com.urbancode.jenkins.plugins.ucdeploy.VersionHelper$VersionBlock',
			componentName: componentName,
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
				baseDir: baseDir,
				fileIncludePatterns: '**/*.war',
				fileExcludePatterns: '',
				pushProperties: 'jenkins.server=Local\njenkins.reviewed=false',
				pushDescription: 'Pushed from Jenkins',
				pushIncremental: false
			]
		]
	])
	
	withEnv(["PATH+UDCLIENT=${tool 'UrbanCodeClient'}"])
	{
		withCredentials([usernamePassword(credentialsId: "${UCD_CREDENTIAL}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')])
		{
			sh "udclient -username ${USERNAME} -password ${PASSWORD} -weburl ${UCD_URL} addVersionStatus -component ${componentName} -version ${version} -status Stable"
		}
	}
}
