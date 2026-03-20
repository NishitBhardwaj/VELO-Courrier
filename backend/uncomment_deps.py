import os

base_path = "src/main/java/com/velo/courrier"

files_to_uncomment = [
    "workers/StaleEventCleanupWorker.java",
    "security/FraudDetectionService.java",
    "growth/DynamicPricingService.java",
    "growth/DriverIncentiveService.java",
    "growth/PromoEngineService.java",
    "events/TransactionalEventPublisher.java",
    "governance/DriverDocumentService.java",
    "events/OutboxRelayScheduler.java",
    "ops/ZoneValidationService.java",
    "events/consumers/NotificationConsumerService.java",
    "enterprise/WebhookRegistrationService.java",
    "enterprise/EnterpriseContractService.java",
    "dispatch/DispatchOverrideService.java",
    "analytics/AnalyticsService.java",
    "ai/AiServiceClient.java",
    "ai/DataExtractionJob.java"
]

for rel_path in files_to_uncomment:
    file_path = os.path.join(base_path, rel_path)
    if not os.path.exists(file_path):
        continue
    
    with open(file_path, "r") as f:
        lines = f.readlines()
        
    new_lines = []
    for line in lines:
        if line.strip().startswith("// private final ") or line.strip().startswith("// import "):
            new_lines.append(line.replace("// ", "", 1))
        # Uncomment block comments surrounding logic (optional, but requested to "check properly connected")
        elif "/*" in line and "*/" in line:
            new_lines.append(line) # Inline block, leave alone
        elif line.strip() == "/*" or line.strip() == "*/":
            pass # Remove multiline scaffold comments entirely bridging the logic
        else:
            new_lines.append(line)
            
    with open(file_path, "w") as f:
        f.writelines(new_lines)
    
print("Uncommented mock dependencies.")
