// Standard Github copyright
// We can change the labels for the nodes as needed

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
  sh "${mvnHome}/bin/mvn install -pl war -am -DskipTests"
  
  stage 'Test'
  //sh "${mvnHome}/bin/mvn -Plight-test install"
 
  archive 'war/target/jenkins.war'
  archive 'rdur/deploy.sh'
}


node ('ubuntu-server') {
    
   stage 'Deploy'
   //checking if there is already Jenkins instance running 
   unarchive mapping: ['war/target/jenkins.war' : '/home/demo/jenkins.war']
  sh "rm -f mypid"
  sh "ps -efwww  |grep -v grep|grep  \"/home/demo/jenkins.war\"|awk {'print \$2'}>mypid"
 
  sh "if [ -s mypid ]; then kill -9  `cat mypid`; fi"
  sh "sleep 10"
   
   sh "nohup java -jar /home/demo/jenkins.war &"
   sh "ls"

}
