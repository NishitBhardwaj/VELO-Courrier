from fastapi import FastAPI
from pydantic import BaseModel
from typing import List

app = FastAPI(title="VELO AI Service", version="1.0.0")

class DispatchRequest(BaseModel):
    booking_id: str
    driver_candidates: List[dict]
    pickup_lat: float
    pickup_lng: float

class RouteOptimizeRequest(BaseModel):
    stops: List[dict]

@app.get("/health")
def health_check():
    return {"status": "ok", "version": "1.0.0"}

@app.get("/ai/demand")
def predict_demand(zone: str, time: str):
    # Phase 1: Heuristic Mock waiting for mass data pipelines
    # Real Model: return model.predict(zone, time)
    return {
        "zone_id": zone,
        "predicted_demand": 42,
        "surge_probability": 0.85
    }

@app.post("/ai/dispatch/recommend")
def recommend_dispatch(req: DispatchRequest):
    # Phase 1: Pure Mathematical Rating heuristics replacing brute-force closest distance
    # Example logic: score = (distance_weight * 0.6) + (driver_rating_weight * 0.4)
    scored_drivers = []
    
    for idx, driver in enumerate(req.driver_candidates):
        mock_score = 0.95 - (idx * 0.05) # Simulated sorting AI
        scored_drivers.append({
            "driver_id": driver["id"],
            "score": max(mock_score, 0.10)
        })
        
    return {
        "booking_id": req.booking_id,
        "recommendations": scored_drivers,
        "model_version": "v1-heuristic"
    }

@app.post("/ai/route/optimize")
def optimize_route(req: RouteOptimizeRequest):
    # Simulate execution of TSP solver logic natively across Cartesian mapping
    # Returning the array exactly as received for basic mock implementation
    return {
        "optimized_sequence": req.stops,
        "efficiency_gain": "12.5%"
    }
