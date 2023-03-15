self.addEventListener('install', (e) => {
  e.waitUntil(
    caches.open('ranstax-store').then((cache) => cache.addAll([
      '/favicon.ico',
      '/index.html',
      '/ranstax.js',
    ])),
  );
});

self.addEventListener('fetch', (e) => {
  console.log(e.request.url);
  e.respondWith(
    caches.match(e.request).then((response) => response || fetch(e.request)),
  );
});