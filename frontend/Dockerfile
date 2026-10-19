# Nginx en Alpine: un servidor web pequeño y rápido para servir un HTML estático
FROM nginx:alpine

# Nuestra configuración: sirve la web y reenvía /api/ al backend
COPY nginx.conf /etc/nginx/conf.d/default.conf

# La web completa es un solo fichero
COPY index.html /usr/share/nginx/html/index.html

# Puerto en el que escucha Nginx dentro del contenedor (documentativo)
EXPOSE 80