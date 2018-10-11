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

String call(String feature, String baseDir)
{
	echo "Running cucumber test for feature: ${feature}."
	
	dir(baseDir)
	{
		sh 'bundle update'
		def statusCode = sh script:"bundle exec cucumber features/${feature}.feature  -f pretty -f html -o logs/${feature}.html -f json -o cucumber.json", returnStatus:true
		cucumber fileIncludePattern: '**/*.json', reducingMethod: 'MERGE_FEATURES_BY_ID', sortingMethod: 'ALPHABETICAL'
		if (statusCode)
		{
			currentBuild.result = 'UNSTABLE'
			error 'Functional test failed.'
		}
	}
}
