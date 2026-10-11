const email = sessionStorage.getItem("userEmail");
if (!email) location.replace("login.html");
document.getElementById("user").textContent = sessionStorage.getItem("userName") || email;
document.getElementById("logoutBtn").onclick = () => { sessionStorage.clear(); location.href = "login.html"; };

const esc = (s) => String(s ?? "").replace(/[&<>"']/g, (c) => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]));
const q = "?email=" + encodeURIComponent(email);

async function refresh() {
  try {
    const [stats, list] = await Promise.all([api("/api/complaints/stats" + q), api("/api/complaints" + q)]);
    ["total", "pending", "resolved"].forEach((k) => (document.getElementById(k).textContent = stats[k]));
    document.getElementById("list").innerHTML = list.length
      ? list.map((c) => `<div class="item"><div><b>${esc(c.title)}</b><br><small>${esc(c.description)}</small></div>
          <div><span class="badge ${esc(c.status)}">${esc(c.status)}</span>
          ${c.status !== "RESOLVED" ? `<button data-id="${c.id}">Mark resolved</button>` : ""}</div></div>`).join("")
      : "No complaints yet.";
  } catch (e) {
    document.getElementById("list").textContent = e.message;
  }
}
document.getElementById("list").addEventListener("click", async (e) => {
  const id = e.target.dataset && e.target.dataset.id;
  if (!id) return;
  try { await api("/api/complaints/" + id + "/resolve", { method: "PUT" }); refresh(); } catch (err) { alert(err.message); }
});
document.getElementById("cForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const msg = document.getElementById("cMsg");
  try {
    await api("/api/complaints", { method: "POST", body: JSON.stringify({ email, title: cTitle.value, description: cDesc.value }) });
    cTitle.value = cDesc.value = "";
    setMsg(msg, "Submitted", true);
    refresh();
  } catch (err) { setMsg(msg, err.message, false); }
});

/* ===== Views: dashboard / complaints / notices / schemes / jobs ===== */
const INFO = {
  notices: { title: "📢 Notices", add: "Add Notice" },
  schemes: { title: "📊 Government Schemes", add: "Add Scheme" },
  jobs: { title: "💼 Jobs", add: "Add Job" },
};
let currentKind = null;

async function loadInfo(kind) {
  currentKind = kind;
  document.getElementById("infoTitle").textContent = INFO[kind].title;
  document.getElementById("infoAddTitle").textContent = INFO[kind].add;
  const box = document.getElementById("infoList");
  box.textContent = "Loading...";
  try {
    const items = await api("/api/info/" + kind);
    box.innerHTML = items.length
      ? items.map((i) => `<div class="item"><div><b>${esc(i.title)}</b><br>${esc(i.description)}<br><small>${esc(i.details)}</small></div></div>`).join("")
      : "Nothing here yet.";
  } catch (e) { box.textContent = e.message; }
}

document.getElementById("infoForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const msg = document.getElementById("iMsg");
  try {
    await api("/api/info/" + currentKind, { method: "POST", body: JSON.stringify({
      title: iTitle.value, description: iDesc.value, details: iDetails.value }) });
    iTitle.value = iDesc.value = iDetails.value = "";
    setMsg(msg, "Added", true);
    loadInfo(currentKind);
  } catch (err) { setMsg(msg, err.message, false); }
});

function showView() {
  const v = (location.hash || "#dashboard").slice(1);
  const view = ["dashboard", "complaints", "notices", "schemes", "jobs", "ai"].includes(v) ? v : "dashboard";
  document.querySelectorAll(".view").forEach((x) => x.classList.remove("active"));
  const isInfo = INFO[view] !== undefined;
  document.getElementById(isInfo ? "view-info" : "view-" + view).classList.add("active");
  document.querySelectorAll("[data-view]").forEach((a) => a.classList.toggle("active", a.dataset.view === view));
  document.getElementById("iMsg").textContent = "";
  if (isInfo) loadInfo(view); else refresh();
}
window.addEventListener("hashchange", showView);
showView();

document.getElementById("aiForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const out = document.getElementById("aiA"), btn = document.getElementById("aiBtn");
  btn.disabled = true; out.textContent = "Soch raha hoon...";
  try {
    const r = await api("/api/ai/ask", { method: "POST", body: JSON.stringify({ question: aiQ.value }) });
    out.textContent = r.answer;
  } catch (err) { out.textContent = err.message; }
  btn.disabled = false;
});
