BEGIN TRANSACTION;

INSERT INTO users (email, role) VALUES ('ssalazar@utem.cl','2');

INSERT INTO categories (token, name, description) VALUES ('501d4fdf5484485abc01','Atención al estudiante','Reclamos sobre la calidad de la atención recibida por parte del personal administrativo, académico o de soporte.');
INSERT INTO categories (token, name, description) VALUES ('e4f4a97320714992b72c','Infraestructura','Problemas o sugerencias relacionados con el estado de las instalaciones físicas, como aulas, laboratorios, bibliotecas, áreas comunes, baños, accesibilidad, etc.');
INSERT INTO categories (token, name, description) VALUES ('429e6510aa23493d89ef','Trámites y procedimientos','Quejas o consultas relacionadas con procesos administrativos como matrículas, certificados, pagos, becas, inscripción de asignaturas, etc.');
INSERT INTO categories (token, name, description) VALUES ('52784cc432b54f8a8591','Servicios tecnológicos','Reportes sobre fallos o mejoras en los sistemas tecnológicos (plataformas de aprendizaje en línea, sistemas de inscripción, correo electrónico, Wi-Fi, etc.).');
INSERT INTO categories (token, name, description) VALUES ('c32ebe4a264f4c6b94e1','Docencia y programas académicos','Reclamos o sugerencias relacionadas con la calidad de la enseñanza, el contenido de los programas académicos, el desempeño de los profesores, carga académica, etc.');
INSERT INTO categories (token, name, description) VALUES ('6860f0b5ae83478289b6','Servicios estudiantiles','Aspectos relacionados con servicios de apoyo, como becas, bienestar estudiantil, orientación académica, actividades extracurriculares, o acceso a recursos.');
INSERT INTO categories (token, name, description) VALUES ('8980d678af66423b9351','Seguridad','Reclamos relacionados con la seguridad en el campus o en los alrededores de la institución, como medidas de seguridad insuficientes o incidentes específicos.');
INSERT INTO categories (token, name, description) VALUES ('f2baf2668952483aafd3','Transparencia y ética','Quejas sobre falta de transparencia en los procesos, favoritismos, conflictos de interés o mala conducta administrativa.');
INSERT INTO categories (token, name, description) VALUES ('3462142cf99d4fe78085','Calidad del servicio','Opiniones o sugerencias sobre la calidad general de los servicios que la institución ofrece.');
INSERT INTO categories (token, name, description) VALUES ('ba4a81bba964489bb554','Medio ambiente y sostenibilidad','Sugerencias o quejas sobre la gestión ambiental, el uso de recursos, reciclaje, o iniciativas sostenibles dentro de la institución.');

    

COMMIT;
