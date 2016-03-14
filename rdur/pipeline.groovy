// Standard Github copyright
// We can change the labels for the nodes as needed

node('master') {
  // Mark the code checkout 'stage'....
  stage 'Checkout'

  // Get some code from a GitHub repository
  git url: 'https://github.com/rainavishal1/jenkins.git'

  // Get the maven tool.
  // ** NOTE: This 'M3' maven tool must be configured
  // **       in the global configuration.           
  def mvnHome = tool 'M3'

  // Mark the code build 'stage'....
  stage 'Build'
  // Run the maven build
  sh "${mvnHome}/bin/mvn install -pl war -am -DskipTests"
  
  stage 'Test'
  //sh "${mvnHome}/bin/mvn -Plight-test install"
 
  archive 'war/target/jenkins.war'
  archive 'rdur/deploy.sh'
}

node ('ubuntu-server') {
   stage 'Deploy'

//   sh "ps -efwww|grep jenkins |grep -v grep|awk '{print \$2}' > ./mypid.txt"

//   sh "if [ -s ./mypid.txt ]; then kill -9 `cat ./mypid.txt`; fi; rm -f ./mypid.txt"
   unarchive mapping: ['war/target/jenkins.war' : '.', 'rdur/deploy.sh' : '.']
   sh "pwd"
  // sh "bash deploy.sh"
//   sh "nohup java -jar jenkins.war &"

}




