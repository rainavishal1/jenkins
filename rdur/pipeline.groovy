// Standard Github copyright

node('master') {
  // Mark the code checkout 'stage'....
  stage 'Checkout'

  // Get some code from a GitHub repository
  git url: 'https://github.com/rduraisamy/jenkins.git'

  // Get the maven tool.
  // ** NOTE: This 'M3' maven tool must be configured
  // **       in the global configuration.           
  def mvnHome = tool 'M3'

  // Mark the code build 'stage'....
  stage 'Build'
  // Run the maven build
  sh "${mvnHome}/bin/mvn clean install -pl war -am -DskipTests"
  
  stage 'Test'
  sh "${mvnHome}/bin/mvn -Plight-test install"
 
  archive 'war/target/jenkins.war'
}

node ('ubuntu-server') {
   stage 'Deploy'
   sh "rm -rf ./war"
   
   unarchive mapping: ['war/target/jenkins.war' : '.']
   sh "ls -lR"
}



