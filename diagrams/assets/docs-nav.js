/**
 * Unified Documentation Sidebar Logic
 * Injected into all 80+ diagram files.
 */

(function() {
    // 1. Create UI Elements
    const toggle = document.createElement('div');
    toggle.id = 'nav-toggle';
    toggle.innerHTML = '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="3" y1="12" x2="21" y2="12"></line><line x1="3" y1="6" x2="21" y2="6"></line><line x1="3" y1="18" x2="21" y2="18"></line></svg>';
    
    const overlay = document.createElement('div');
    overlay.id = 'nav-overlay';
    
    const sidebar = document.createElement('div');
    sidebar.id = 'docs-sidebar';
    sidebar.innerHTML = `
        <div class="nav-header">
            <h3>VELO Documentation</h3>
            <a href="HOME_PATH" class="home-link">🏠 Back to Dashboard</a>
        </div>
        <div class="nav-search">
            <input type="text" id="nav-search-input" placeholder="Search diagrams...">
        </div>
        <div class="nav-list" id="nav-list">
            <div style="padding: 20px; color: #64748b;">Loading manifest...</div>
        </div>
    `;

    document.body.appendChild(toggle);
    document.body.appendChild(overlay);
    document.body.appendChild(sidebar);

    // 2. Fetch Manifest & Build List
    const currentPath = window.location.pathname;
    // Determine depth to find manifest.json
    let rootPath = './';
    if (currentPath.includes('/layer-')) {
        const parts = currentPath.split('/');
        const layerIndex = parts.findIndex(p => p.startsWith('layer-'));
        const subIndex = parts.findIndex(p => p.endsWith('.html'));
        const depth = subIndex - layerIndex;
        rootPath = '../'.repeat(depth + 1);
    }
    
    // Auto-fix HOME_PATH
    sidebar.innerHTML = sidebar.innerHTML.replace('HOME_PATH', rootPath + 'index.html');

    const data = [
  {
    "id": "1.01",
    "title": "1.01 System Overview Diagram",
    "category": "🔷 Layer 1 — SDE / System Design",
    "path": "layer-1-sde/1.01-system-overview.html",
    "layer": 1
  },
  {
    "id": "1.02",
    "title": "1.02 High-Level Architecture Diagram",
    "category": "🔷 Layer 1 — SDE / System Design",
    "path": "layer-1-sde/1.02-architecture.html",
    "layer": 1
  },
  {
    "id": "1.03",
    "title": "1.03 Module Breakdown Diagram",
    "category": "🔷 Layer 1 — SDE / System Design",
    "path": "layer-1-sde/1.03-module-breakdown.html",
    "layer": 1
  },
  {
    "id": "1.04",
    "title": "1.04 Microservice vs Monolith Decision",
    "category": "🔷 Layer 1 — SDE / System Design",
    "path": "layer-1-sde/1.04-microservice-vs-monolith.html",
    "layer": 1
  },
  {
    "id": "1.05",
    "title": "1.05 Database Entity Relationship Diagram",
    "category": "🔷 Layer 1 — SDE / System Design",
    "path": "layer-1-sde/1.05-er-diagram.html",
    "layer": 1
  },
  {
    "id": "1.06",
    "title": "1.06 API Interaction Diagram",
    "category": "🔷 Layer 1 — SDE / System Design",
    "path": "layer-1-sde/1.06-api-interaction.html",
    "layer": 1
  },
  {
    "id": "1.07",
    "title": "1.07 External Integration Diagram",
    "category": "🔷 Layer 1 — SDE / System Design",
    "path": "layer-1-sde/1.07-external-integration.html",
    "layer": 1
  },
  {
    "id": "1.08",
    "title": "1.08 Deployment / Cloud Infrastructure Diagram",
    "category": "🔷 Layer 1 — SDE / System Design",
    "path": "layer-1-sde/1.08-deployment.html",
    "layer": 1
  },
  {
    "id": "1.09",
    "title": "1.09 Security & Access Control Diagram",
    "category": "🔷 Layer 1 — SDE / System Design",
    "path": "layer-1-sde/1.09-security-access-control.html",
    "layer": 1
  },
  {
    "id": "1.10",
    "title": "1.10 CI/CD & Environment Flow Diagram",
    "category": "🔷 Layer 1 — SDE / System Design",
    "path": "layer-1-sde/1.10-cicd-flow.html",
    "layer": 1
  },
  {
    "id": "2.01",
    "title": "2.01 End-to-End Platform Flow",
    "category": "🟢 Layer 2 — Flowcharts / Master Flows",
    "path": "layer-2-flowcharts/master/2.01-end-to-end-platform.html",
    "layer": 2
  },
  {
    "id": "2.02",
    "title": "1.1 User Registration & Login Flow",
    "category": "🧠 1. Core System Flows",
    "path": "layer-2-flowcharts/master/2.02-registration-login.html",
    "layer": 2
  },
  {
    "id": "2.03",
    "title": "1.3 Booking Lifecycle Flow",
    "category": "🧠 1. Core System Flows",
    "path": "layer-2-flowcharts/master/2.03-booking-lifecycle.html",
    "layer": 2
  },
  {
    "id": "2.04",
    "title": "1.4 Driver Matching & Dispatch Flow",
    "category": "🧠 1. Core System Flows",
    "path": "layer-2-flowcharts/master/2.04-driver-dispatch.html",
    "layer": 2
  },
  {
    "id": "2.05",
    "title": "1.5 Payment Processing Flow",
    "category": "🧠 1. Core System Flows",
    "path": "layer-2-flowcharts/master/2.05-payment.html",
    "layer": 2
  },
  {
    "id": "2.10",
    "title": "2.10 Instant Goods Transport Flow",
    "category": "🟢 Layer 2 — Service-Specific Flows",
    "path": "layer-2-flowcharts/service-specific/2.10-instant-goods-transport.html",
    "layer": 2
  },
  {
    "id": "2.11",
    "title": "2.11 Packers & Movers Flow",
    "category": "🟢 Layer 2 — Service-Specific Flows",
    "path": "layer-2-flowcharts/service-specific/2.11-packers-movers.html",
    "layer": 2
  },
  {
    "id": "2.12",
    "title": "2.12 Courier Delivery Flow",
    "category": "🟢 Layer 2 — Service-Specific Flows",
    "path": "layer-2-flowcharts/service-specific/2.12-courier-delivery.html",
    "layer": 2
  },
  {
    "id": "2.13",
    "title": "2.13 Enterprise Logistics Flow",
    "category": "🟢 Layer 2 — Service-Specific Flows",
    "path": "layer-2-flowcharts/service-specific/2.13-enterprise-logistics.html",
    "layer": 2
  },
  {
    "id": "2.14",
    "title": "1.2 Authentication & Role Routing Flow",
    "category": "🧠 1. Core System Flows",
    "path": "layer-2-flowcharts/master/2.14-auth-role-routing.html",
    "layer": 2
  },
  {
    "id": "2.15",
    "title": "1.6 Notification System Flow",
    "category": "🧠 1. Core System Flows",
    "path": "layer-2-flowcharts/master/2.15-notification-system.html",
    "layer": 2
  },
  {
    "id": "3.01",
    "title": "3.01 System Use Case Diagram",
    "category": "🟣 Layer 3 — UML / Use Cases",
    "path": "layer-3-uml/use-cases/3.01-system-use-case.html",
    "layer": 3
  },
  {
    "id": "3.02",
    "title": "3.02 Customer Use Case Diagram",
    "category": "🟣 Layer 3 — UML / Use Cases",
    "path": "layer-3-uml/use-cases/3.02-customer-use-case.html",
    "layer": 3
  },
  {
    "id": "3.03",
    "title": "3.03 Driver Use Case Diagram",
    "category": "🟣 Layer 3 — UML / Use Cases",
    "path": "layer-3-uml/use-cases/3.03-driver-use-case.html",
    "layer": 3
  },
  {
    "id": "3.04",
    "title": "3.04 Business User Use Case Diagram",
    "category": "🟣 Layer 3 — UML / Use Cases",
    "path": "layer-3-uml/use-cases/3.04-business-use-case.html",
    "layer": 3
  },
  {
    "id": "3.05",
    "title": "3.05 Admin Use Case Diagram",
    "category": "🟣 Layer 3 — UML / Use Cases",
    "path": "layer-3-uml/use-cases/3.05-admin-use-case.html",
    "layer": 3
  },
  {
    "id": "3.06",
    "title": "3.06 Booking Sequence Diagram",
    "category": "🟣 Layer 3 — UML / Sequence Diagrams",
    "path": "layer-3-uml/sequence/3.06-booking-sequence.html",
    "layer": 3
  },
  {
    "id": "3.07",
    "title": "3.07 Driver Dispatch Sequence Diagram",
    "category": "🟣 Layer 3 — UML / Sequence Diagrams",
    "path": "layer-3-uml/sequence/3.07-driver-dispatch-sequence.html",
    "layer": 3
  },
  {
    "id": "3.08",
    "title": "3.08 Payment Sequence Diagram",
    "category": "🟣 Layer 3 — UML / Sequence Diagrams",
    "path": "layer-3-uml/sequence/3.08-payment-sequence.html",
    "layer": 3
  },
  {
    "id": "3.09",
    "title": "3.09 Tracking Sequence Diagram",
    "category": "🟣 Layer 3 — UML / Sequence Diagrams",
    "path": "layer-3-uml/sequence/3.09-tracking-sequence.html",
    "layer": 3
  },
  {
    "id": "3.10",
    "title": "3.10 Refund & Dispute Sequence Diagram",
    "category": "🟣 Layer 3 — UML / Sequence Diagrams",
    "path": "layer-3-uml/sequence/3.10-refund-dispute-sequence.html",
    "layer": 3
  },
  {
    "id": "3.11",
    "title": "3.11 Customer Activity Diagram",
    "category": "🟣 Layer 3 — UML / Activity Diagrams",
    "path": "layer-3-uml/activity/3.11-customer-activity.html",
    "layer": 3
  },
  {
    "id": "3.12",
    "title": "3.12 Driver Activity Diagram",
    "category": "🟣 Layer 3 — UML / Activity Diagrams",
    "path": "layer-3-uml/activity/3.12-driver-activity.html",
    "layer": 3
  },
  {
    "id": "3.13",
    "title": "3.13 Business Booking Activity Diagram",
    "category": "🟣 Layer 3 — UML / Activity Diagrams",
    "path": "layer-3-uml/activity/3.13-business-activity.html",
    "layer": 3
  },
  {
    "id": "3.14",
    "title": "3.14 Admin Activity Diagram",
    "category": "🟣 Layer 3 — UML / Activity Diagrams",
    "path": "layer-3-uml/activity/3.14-admin-activity.html",
    "layer": 3
  },
  {
    "id": "3.15",
    "title": "3.15 Class Diagram",
    "category": "🟣 Layer 3 — UML / Structural",
    "path": "layer-3-uml/structural/3.15-class-diagram.html",
    "layer": 3
  },
  {
    "id": "3.16",
    "title": "3.16 Component Diagram",
    "category": "🟣 Layer 3 — UML / Structural",
    "path": "layer-3-uml/structural/3.16-component-diagram.html",
    "layer": 3
  },
  {
    "id": "3.17",
    "title": "3.17 Deployment Diagram (UML)",
    "category": "🟣 Layer 3 — UML / Structural",
    "path": "layer-3-uml/structural/3.17-deployment-uml.html",
    "layer": 3
  },
  {
    "id": "3.18",
    "title": "3.18 Booking State Machine Diagram",
    "category": "🟣 Layer 3 — UML / State Machines",
    "path": "layer-3-uml/state-machines/3.18-booking-state-machine.html",
    "layer": 3
  },
  {
    "id": "3.19",
    "title": "3.19 Driver Availability State Machine Diagram",
    "category": "🟣 Layer 3 — UML / State Machines",
    "path": "layer-3-uml/state-machines/3.19-driver-state.html",
    "layer": 3
  },
  {
    "id": "3.20",
    "title": "3.20 Package Diagram",
    "category": "🟣 Layer 3 — UML / Structural",
    "path": "layer-3-uml/structural/3.20-package-diagram.html",
    "layer": 3
  },
  {
    "id": "4.1",
    "title": "4.1 Customer App Navigation Map",
    "category": "🟠 Layer 4 — UI Navigation Maps",
    "path": "layer-4-navigation/4.1-customer-nav.html",
    "layer": 4
  },
  {
    "id": "4.2",
    "title": "4.2 Driver App Navigation Map",
    "category": "🟠 Layer 4 — UI Navigation Maps",
    "path": "layer-4-navigation/4.2-driver-nav.html",
    "layer": 4
  },
  {
    "id": "4.3",
    "title": "4.3 Business Dashboard Navigation Map",
    "category": "🟠 Layer 4 — UI Navigation Maps",
    "path": "layer-4-navigation/4.3-business-nav.html",
    "layer": 4
  },
  {
    "id": "4.4",
    "title": "4.4 Admin Panel Navigation Map",
    "category": "🟠 Layer 4 — UI Navigation Maps",
    "path": "layer-4-navigation/4.4-admin-nav.html",
    "layer": 4
  }
];
    renderList(data, rootPath);
    setupSearch(data, rootPath);

    function renderList(data, root) {
        const list = document.getElementById('nav-list');
        list.innerHTML = '';
        
        const categories = {
            "SDE": data.filter(i => i.layer === 1),
            "Flowcharts": data.filter(i => i.layer === 2),
            "UML": data.filter(i => i.layer === 3),
            "UI/Nav": data.filter(i => i.layer === 4)
        };

        for (const [cat, items] of Object.entries(categories)) {
            if (items.length === 0) continue;
            
            const catDiv = document.createElement('div');
            catDiv.className = 'nav-category';
            catDiv.textContent = cat;
            list.appendChild(catDiv);

            items.forEach(item => {
                const a = document.createElement('a');
                a.className = 'nav-item';
                if (currentPath.endsWith(item.path.split('/').pop())) {
                    a.classList.add('active');
                }
                a.href = root + item.path;
                a.innerHTML = `<span style="opacity:0.5; font-weight:700; margin-right:8px;">${item.id}</span> ${item.title}`;
                list.appendChild(a);
            });
        }
    }

    function setupSearch(data, root) {
        const input = document.getElementById('nav-search-input');
        input.addEventListener('input', (e) => {
            const val = e.target.value.toLowerCase();
            const filtered = data.filter(i => 
                i.title.toLowerCase().includes(val) || 
                i.id.toString().includes(val) ||
                i.category.toLowerCase().includes(val)
            );
            renderList(filtered, root);
        });
    }

    // 3. Interactions
    toggle.addEventListener('click', () => {
        sidebar.classList.toggle('open');
        overlay.classList.toggle('show');
    });

    overlay.addEventListener('click', () => {
        sidebar.classList.remove('open');
        overlay.classList.remove('show');
    });

})();
