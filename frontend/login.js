document.getElementById("loginForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const email = document.getElementById("loginEmail").value.trim();
  const password = document.getElementById("loginPassword").value;
  const msg = document.getElementById("msg");
  const eErr = document.getElementById("loginEmailError");
  const pErr = document.getElementById("loginPassError");
  eErr.textContent = pErr.textContent = "";
  let ok = true;
  if (!/^[^ @]+@[^ @]+\.[a-zA-Z]{2,}$/.test(email)) { eErr.textContent = "Invalid email"; ok = false; }
  if (password.length < 6) { pErr.textContent = "Password must be 6+ chars"; ok = false; }
  if (!ok) return;
  const btn = document.getElementById("loginBtn");
  btn.disabled = true;
  try {
    const data = await api("/api/login", { method: "POST", body: JSON.stringify({ email, password }) });
    sessionStorage.setItem("userEmail", data.email);
    sessionStorage.setItem("userName", data.name);
    setMsg(msg, "Login successful! Redirecting...", true);
    setTimeout(() => (location.href = "dashboard.html"), 600);
  } catch (err) {
    setMsg(msg, err.message, false);
    btn.disabled = false;
  }
});
