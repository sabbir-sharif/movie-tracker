const BASE_URL = "http://localhost:8080";

// 🔐 LOGIN
document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const data = {
        email: document.getElementById("loginEmail").value,
        password: document.getElementById("loginPassword").value
    };

    const res = await fetch(BASE_URL + "/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify(data)
    });

    const text = await res.text();

    if (res.ok) {
        alert("✨ Welcome Wizard!");
        window.location.href = "/app.html"; // your main app
    } else {
        alert(text);
    }
});


// 🪄 SIGNUP
document.getElementById("signupForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const data = {
        name: document.getElementById("signupName").value,
        email: document.getElementById("signupEmail").value,
        password: document.getElementById("signupPassword").value
    };

    const res = await fetch(BASE_URL + "/auth/signup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });

    const text = await res.text();

    if (res.ok) {
        alert("🪶 Account created! Now login.");
    } else {
        alert(text);
    }
});