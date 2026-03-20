import os

base_path = "src/main/java/com/velo/courrier"

# Map of package -> entities to generate (if not exists)
# format: { package: [(EntityName, [fields])] }
entities_to_gen = {
    "growth": [
        ("PricingSurgeLog", ["UUID id", "UUID regionId", "java.math.BigDecimal multiplier"]),
        ("DriverIncentive", ["UUID id", "UUID driverId", "String campaignName", "java.math.BigDecimal bonusAmount", "String status"]),
        ("PromoUsage", ["UUID id", "UUID promoCodeId", "UUID customerId"])
    ],
    "events": [
        ("NotificationQueue", ["UUID id", "UUID userId", "String type", "String payload", "String status"])
    ],
    "enterprise": [
        ("EnterpriseWebhook", ["UUID id", "UUID customerId", "String eventType", "String callbackUrl", "Boolean active"])
    ],
    "ai": [
        ("MlTrainingData", ["UUID bookingId", "String pickupZone", "String dropZone", "Integer delaySeconds"])
    ],
    "analytics": [
        ("ZoneEvent", ["UUID id", "UUID driverId", "UUID zoneId"])
    ],
    "security": [
        ("UserTrustScore", ["UUID id", "UUID userId", "Integer score"])
    ]
}

repos_to_gen = {
    "predictive": ["EtaTrackingLogRepository", "RouteDeviationEventRepository"],
    "growth": ["SurgeLogRepository", "OperationalRegionRepository", "DriverIncentiveRepository", "PromoCodeRepository", "PromoUsageRepository"],
    "events": ["EventOutboxRepository", "NotificationQueueRepository"],
    "governance": ["DriverDocumentRepository"],
    "enterprise": ["EnterpriseContractRepository", "EnterpriseWebhookRepository"],
    "dispatch": ["DispatchOverrideEventRepository"],
    "ai": ["MlTrainingDataRepository"],
    "analytics": ["ZoneEventRepository"],
    "security": ["UserTrustScoreRepository"]
}

repo_entity_map = {
    "EtaTrackingLogRepository": "EtaTrackingLog",
    "RouteDeviationEventRepository": "RouteDeviationEvent",
    "SurgeLogRepository": "PricingSurgeLog",
    "OperationalRegionRepository": "OperationalRegion",
    "DriverIncentiveRepository": "DriverIncentive",
    "PromoCodeRepository": "PromoCode",
    "PromoUsageRepository": "PromoUsage",
    "EventOutboxRepository": "EventOutbox",
    "NotificationQueueRepository": "NotificationQueue",
    "DriverDocumentRepository": "DriverDocument",
    "EnterpriseContractRepository": "EnterpriseContract",
    "EnterpriseWebhookRepository": "EnterpriseWebhook",
    "DispatchOverrideEventRepository": "DispatchOverrideEvent",
    "MlTrainingDataRepository": "MlTrainingData",
    "ZoneEventRepository": "ZoneEvent",
    "UserTrustScoreRepository": "UserTrustScore"
}

def generate_entity(pkg, entity_name, fields):
    dir_path = os.path.join(base_path, pkg)
    os.makedirs(dir_path, exist_ok=True)
    file_path = os.path.join(dir_path, f"{entity_name}.java")
    if os.path.exists(file_path): return

    field_strs = ""
    for f in fields:
        parts = f.split(" ")
        ftype = parts[0]
        fname = parts[1]
        field_strs += f"    @Column(name = \"{fname}\")\n    private {ftype} {fname};\n\n"

    content = f"""package com.velo.courrier.{pkg};

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class {entity_name} {{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

{field_strs}
}}
"""
    with open(file_path, "w") as f:
        f.write(content)
    print(f"Generated Entity: {entity_name}.java")

def generate_repo(pkg, repo_name, entity_name):
    dir_path = os.path.join(base_path, pkg)
    os.makedirs(dir_path, exist_ok=True)
    file_path = os.path.join(dir_path, f"{repo_name}.java")
    if os.path.exists(file_path): return

    content = f"""package com.velo.courrier.{pkg};

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface {repo_name} extends JpaRepository<{entity_name}, UUID> {{
}}
"""
    with open(file_path, "w") as f:
        f.write(content)
    print(f"Generated Repository: {repo_name}.java")

# 1. Generate Entities
for pkg, entities in entities_to_gen.items():
    for ent in entities:
        generate_entity(pkg, ent[0], ent[1])

# 2. Generate Repositories
for pkg, repos in repos_to_gen.items():
    for repo in repos:
        ent = repo_entity_map[repo]
        generate_repo(pkg, repo, ent)

print("Codegen complete.")
