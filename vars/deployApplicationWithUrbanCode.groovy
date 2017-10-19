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

def call(UrbanCodeConfiguration config, DeployApplication app, String environment)
{
	echo "Deploying application via Urban Code Deploy: ${app.name}."
	
    step([$class: 'UCDeployPublisher',
		siteName: config.site,
		deploy: [
			$class: 'com.urbancode.jenkins.plugins.ucdeploy.DeployHelper$DeployBlock',
			deployApp: app.name,
			deployEnv: environment,
			deployProc: app.process,
			deployVersions: app.componentSpecification,
			deployOnlyChanged: false
		]
	])
}
