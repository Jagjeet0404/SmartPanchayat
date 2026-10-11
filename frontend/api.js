async function api(path, options = {}) {
  let res;
  try {
    res = await fetch(window.API_BASE + path, {
      headers: { "Content-Type": "application/json" },
      ...options,
    });
  } catch (e) {
    throw new Error("Cannot reach server. Is the backend running?");
  }
  let data = null;
  try { data = await res.json(); } catch (e) {}
  if (!res.ok) throw new Error((data && data.error) || "Request failed (" + res.status + ")");
  return data;
}
function setMsg(el, text, ok) {
  el.textContent = text;
  el.className = "msg " + (ok ? "ok" : "bad");
}
