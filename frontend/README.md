# Front-end do Sistema de Chamados (React + Vite)

1. Suba a API Spring Boot: `./mvnw spring-boot:run` (porta 8080).
2. Neste projeto: `npm install` e depois `npm run dev`.
3. Abra http://localhost:5173

O `vite.config.js` encaminha /clientes, /categorias, /tecnicos e /chamados para
http://localhost:8080, por isso não é preciso configurar CORS no Spring.
Cadastre nesta ordem: cliente, categoria, (técnico), chamado.
