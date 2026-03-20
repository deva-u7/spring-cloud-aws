# spring-cloud-aws

Multi-module Spring Boot 3 + Java 21 project demonstrating AWS service integrations. Each module is a runnable Spring Boot application covering a distinct AWS service pattern.

## Modules

| Module | AWS Service | Description |
|---|---|---|
| `sns-messaging` | Amazon SNS | Publish/subscribe topic messaging |
| `sqs-messaging` | Amazon SQS | Queue-based async messaging |
| `s3-document-service` | Amazon S3 | File upload/download with LocalStack E2E tests |
| `secrets-manager` | AWS Secrets Manager | Inject secrets into Spring environment at startup |
| `feature-flags` | LaunchDarkly | Feature flag evaluation per user/country |

## Tech Stack

- **Java 21**, **Spring Boot 3.3**
- **AWS SDK v2** (`software.amazon.awssdk`)
- **Spring Cloud AWS 3.3** (`io.awspring.cloud`) for SNS/SQS
- **Testcontainers + LocalStack** for S3 E2E tests
- **MapStruct** for DTO mapping
- **LaunchDarkly SDK 7** with `LDContext` API

## Quick Start

Each module is self-contained. Set the required environment variables and run:

```bash
# Build all modules
mvn clean package -DskipTests

# Run a specific module
cd sns-messaging
mvn spring-boot:run
```

## Environment Variables

| Variable | Modules | Description |
|---|---|---|
| `AWS_ACCESS_KEY` | sns, sqs, s3 | AWS access key |
| `AWS_SECRET_KEY` | sns, sqs, s3 | AWS secret key |
| `AWS_REGION` | sns, sqs, s3 | AWS region (default: `us-east-1`) |
| `SNS_TOPIC_ARN` | sns | Full topic ARN |
| `SQS_QUEUE_NAME` | sqs | Queue name (default: `DemoQueue`) |
| `AWS_S3_BUCKET` | s3 | S3 bucket name |
| `AWS_SECRET_NAME` | secrets | Secrets Manager secret name |
| `LAUNCH_DARKLY_SDK_KEY` | feature-flags | LaunchDarkly server-side SDK key |

## Module Details

### sns-messaging
Publish messages and subscribe emails to an SNS topic via REST endpoints.

```bash
POST /subscribe/{email}    # subscribe email to topic
POST /publish?message=...  # publish message to topic
```

### sqs-messaging
Send messages to SQS and receive them via a `@SqsListener`.

```bash
GET /api/send/{message}    # send message to queue
```

### s3-document-service
Upload files to S3 and retrieve them by ID. Includes full E2E integration tests using Testcontainers + LocalStack (no real AWS needed for tests).

```bash
POST /api/v1/document      # upload document + file
GET  /api/v1/document/{id} # download document
```

### secrets-manager
Uses `EnvironmentPostProcessor` to load secrets from AWS Secrets Manager and inject them as Spring properties before the application context starts.

```bash
GET /api/sms/{id}          # fetch SMS reminder record
```

### feature-flags
Evaluates LaunchDarkly feature flags per user/country context.

```bash
GET /api/test?country=US   # evaluate flag with country context
GET /api/test1             # evaluate flag without context
```

## Running S3 Tests

The `s3-document-service` module has E2E integration tests that use LocalStack via Testcontainers — no AWS account needed:

```bash
cd s3-document-service
mvn test
```

Requires Docker to be running.
