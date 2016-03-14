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
   unarchive mapping: ['war/target/jenkins.war' : '.']

}




