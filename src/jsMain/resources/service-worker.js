self.addEventListener('install', (e) => {
  e.waitUntil(
    caches.open('ranstax-store').then((cache) => cache.addAll([
      '/ico/ranstax32px.png',
      '/ico/ranstax48px.png',
      '/ico/ranstax64px.png',
      '/ico/ranstax96px.png',
      '/ico/ranstax128px.png',
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
