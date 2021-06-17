Steps for the Project -

1. Create a infrastructure to host the service in ECS 

		aws cloudformation create-stack --stack-name EStockAppCoreStack --capabilities CAPABILITY_NAMED_IAM --template-body file://~/environment/aws_newway/cfn/core.yml

	
	To check the stack status
		aws cloudformation describe-stacks --stack-name EStockAppCoreStack

	To get the values for each service in stack we can output them to a JSON file using the below command.
		aws cloudformation describe-stacks --stack-name EStockAppCoreStack > ~/environment/cloudformation-core-output.json

2.	Update to JDK 8 and Apache maven

		sudo yum -y install java-1.8.0-openjdk-devel
		
		sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
		sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
		sudo yum install -y apache-maven

3.	Now we will build the services and create the docker image for each service.
		
		mvn clean install -DskipTests
		docker build . -t {ACCOUNT_ID}.dkr.ecr.{REGION}.amazonaws.com/{SERVICE_NAME}/service:latest
		
	Run the above command for all the services to create dockers.
	
4.	Run the docker images to test them locally
	
		docker run -p 8080:8080 {REPLACE_WITH_DOCKER_IMAGE_TAG}
		
5.	Now that we have created the images we can push them to Container Repository in Amazon.

	To create a repository
		aws ecr create-repository --repository-name {SERVICE_NAME}/service
	
	Now we will login into the ecr using the following command.
		$(aws ecr get-login --no-include-email)
	
	Now we will push the docker images to the repository
		docker push {REPLACE_WITH_DOCKER_IMAGE_TAG}
		
6.	Now Lets create a ECS Cluster
		aws ecs create-cluster --cluster-name {CLUSTER_NAME}
		
	Also create a cloud-watch log to see whatever happens inside the cluster-name
		aws logs create-log-group --log-group-name {NAME_OF_THE_LOG_GROUP}
	
	Now we will create a task-definition file and we will define what all the container images need to be hosted inside the cluster. Task definition file can be found in the attachments. We need to mention all the docker images we want to run inside .
	
		aws ecs register-task-definition --cli-input-json file://~/environment/{LOCATION_OF_THE_TASK_DEFINITION_FILE}/task-definition.json
		
7.	Enabling the load balancer for the cluster services. 
	NOTE : loadbalancer can we internet-facing/internal . Im creating internet-facing to validate other services. 
	
	aws elbv2 create-load-balancer --name {NAME} --scheme internet-facing --type network --subnets REPLACE_ME_PUBLIC_SUBNET_ONE REPLACE_ME_PUBLIC_SUBNET_TWO > ~/environment/nlb-output.json
	
	We need to create target groups to register our services to the load balancer.
	
		aws elbv2 create-target-group --name {NAME}-TargetGroup --port 8080 --protocol TCP --target-type ip --vpc-id REPLACE_ME_VPC_ID --health-check-interval-seconds 10 --health-check-path / --health-check-protocol HTTP --healthy-threshold-count 3 --unhealthy-threshold-count 3 > ~/environment/target-group-output.json
	
	You can create multiple target groups for each container 
	
	Now we need to create a listener so that the nlb can redirect the traffic.
	
		aws elbv2 create-listener --default-actions TargetGroupArn=REPLACE_ME_NLB_TARGET_GROUP_ARN,Type=forward --load-balancer-arn REPLACE_ME_NLB_ARN --port 80 --protocol TCP      (Check in target-group-output.json) and nlb in (nlb-output.json)

8.	Create a IAM role for ecs services if not present
	aws iam create-service-linked-role --aws-service-name ecs.amazonaws.com
	
9.	Till now we created a cluster and a task . Task cannot run on its own. It needs a service. we need to define the services.
	Service-definition file is attached.
		
		aws ecs create-service --cli-input-json file://~/environment/{location}/service-definition.json
	
------------------------ WE ARE SUCCESSFULLY RUNNING OUR SERVICES ON ECS THAT IS BEHIND A NETWORK LOAD BALANCER -------------------------

DYNAMO DB CONFIGURATION : - 

1.	To Create a dynamodb table
		aws dynamodb create-table --cli-input-json file://~/environment/{location}/dynamodb-table.json
		
2.	To add values to ur table
		aws dynamodb batch-write-item --request-items file://~/environment/{location}/populate-dynamodb.json
		
Both dynamodb-table.json and populate-dynamodb.json are attached.

-----------------------------------------------------------------------------------------------------------------------------------------

AWS COGNITO USER POOL

1.	Now lets create a user pool

		aws cognito-idp create-user-pool --pool-name CompanyServiceUserPool --auto-verified-attributes email

2.	For creating pool
		aws cognito-idp create-user-pool-client --user-pool-id ap-south-1_mqYIgtANu --client-name CompanyServiceUserPoolClient
		
------------------------------------------------------------------------------------------------------------------------------------------
FOR API GATEWAY

We will create a api gateway inside the vpc using vpclink to communciate with the nlb

		aws apigateway create-vpc-link --name MysfitsApiVpcLink --target-arns REPLACE_ME_NLB_ARN > ~/environment/api-gateway-link-output.json
	
		