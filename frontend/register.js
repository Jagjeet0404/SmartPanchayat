document.getElementById("registerForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const v = (id) => document.getElementById(id).value;
  const name = v("name").trim(), email = v("email").trim(), phone = v("phone").trim();
  const password = v("password"), confirm = v("confirmPassword");
  const err = (id, t) => (document.getElementById(id).textContent = t);
  ["nameError", "emailError", "phoneError", "passError", "confirmError"].forEach((i) => err(i, ""));
  let ok = true;
  if (name.length < 3) { err("nameError", "Name too short"); ok = false; }
  if (!/^[^ @]+@[^ @]+\.[a-zA-Z]{2,}$/.test(email)) { err("emailError", "Invalid email"); ok = false; }
  if (!/^[0-9]{10}$/.test(phone)) { err("phoneError", "Invalid phone number"); ok = false; }
  if (password.length < 6) { err("passError", "Password must be 6+ chars"); ok = false; }
  if (password !== confirm) { err("confirmError", "Passwords do not match"); ok = false; }
  if (!ok) return;
  const box = document.getElementById("successBox");
  const btn = document.getElementById("regBtn");
  btn.disabled = true;
  try {
    await api("/api/register", { method: "POST", body: JSON.stringify({ name, email, phone, password }) });
    setMsg(box, "Registration successful! Redirecting...", true);
    setTimeout(() => (location.href = "login.html"), 1200);
  } catch (e2) {
    setMsg(box, e2.message, false);
    btn.disabled = false;
  }
});
