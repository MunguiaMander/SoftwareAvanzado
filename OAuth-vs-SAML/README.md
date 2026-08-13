# OAuth vs SAML

> Video: https://drive.google.com/file/d/1v_5Ze-Wxk0fu7rTVxu0Q90fU-AosZcTX/view?usp=sharing

## Conceptos

**Delegación de identidad**: la app no guarda contraseñas. Redirige al usuario a un proveedor de identidad, que autentica y devuelve una prueba firmada. La app solo verifica la firma.

**Single Sign-On**: la sesión maestra vive en el proveedor. Al entrar a una segunda app, el proveedor reconoce esa sesión y devuelve la identidad sin volver a pedir credenciales.

**Qué resuelven**: una sola superficie donde viven las credenciales, MFA configurado una vez, alta y baja de usuarios centralizada, y auditoría en un solo sitio.

**Los dos ecosistemas**: OAuth (2012) nació en la web social y móvil JSON, tokens compactos, APIs. SAML (2005) nació en la empresa XML, firmas, navegador. Siguen coexistiendo porque el software corporativo lleva 20 años integrando SAML.


> **La assertion SAML pesa 11.5× más que el access token de OAuth**, transportando
> esencialmente la misma identidad.

## OAuth 2.0

**Funcionalidades**: Authorization Code + PKCE obligatorio (`S256`), login OIDC (open source), access tokens JWT con scopes y pantalla de consentimiento, Resource Server, stateless validado y discovery automático.

**Beneficios**: token compacto que cabe en una cabecera  **el único de los dos que sirve para llamar APIs** en nombre del usuario  autorización granular con consentimiento explícito   discovery y rotación de claves sin mantenimiento  natural en SPAs, móviles y microservicios.

**Complejidades**: OAuth **no autentica** — usar un access token como prueba de identidad es el error de diseño más común de la industria, y por eso existe OIDC más piezas móviles que un SSO SAML simple   los JWT **no se revocan** antes de expirar sin introspección o listas de revocación   dónde guardar los tokens en el navegador es un
problema real.

**Cuándo usarlo**: apps móviles y SPAs   APIs y microservicios   login social arquitecturas nuevas   **acceso delegado de terceros a datos del usuario**, el caso que solo OAuth resuelve.

---

## SAML 2.0

**Funcionalidades**: Web Browser SSO iniciado por el SP con `AuthnRequest` firmado assertion firmada con `Conditions`, `AudienceRestriction` y `AuthnStatement` `AttributeStatement` con atributos de directorio y roles  intercambio de metadata Source Provider y el Identification Provider  
Single Log-Out en el estándar.

**Beneficios**: atributos de directorio ricos en el propio login (5 en esta POC: `mail`,`givenName`, `surname`, `department`, `Role`)   SSO empresarial maduro con 20 años de integraciones   SLO definido en el estándar   criptográficamente sólido y muy auditado.

**Complejidades**: XML 11.5× más pesado   **certificados gestionados a mano**  cuando caducan, el SSO se cae, y es la causa de incidencias en producción el metadata se lee una sola vez al arrancar, así que rotar el certificado del IdP **obliga a reiniciar** el SP   OpenSAML no está en Maven Central   superficie de ataque propia de XML (XML Signature Wrapping, XXE, canonicalización)   **CORS**: el POST del IdP al ACS llega con el Origin del IdP, así que CORS debe limitarse a las rutas de API o el login falla con 403   no sirve para APIs, móviles ni SPAs.

**Cuándo usarlo**: SSO corporativo con un IdP existente (ADFS, Okta)   SaaS B2B donde el cliente empresarial **exige** SAML   federaciones académicas y de gobierno   apps web internas legadas   cuando se necesitan atributos de directorio ricos en el login.

---

## Conclusión

OAuth y SAML **no son competidores directos**. SAML es SSO web empresarial; OAuth es autorización delegada para APIs. A pesar de que SAML es el que esta dentro de los corporativos OAuth es mas moderno y representa una ganancia enorme en cuanto a rendimiento cuando se tiene recursos intermedios limitados ya que su Assertion es mas económico.

