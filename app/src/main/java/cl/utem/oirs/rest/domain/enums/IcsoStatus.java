package cl.utem.oirs.rest.domain.enums;

/**
 *
 * @author seba
 */
public enum IcsoStatus {
    ERROR {
        @Override
        public String getLabel() {
            return "Error";
        }

        @Override
        public String getDescription() {
            return "Error";
        }

        @Override
        public boolean isFinal() {
            return true;
        }
    }, RECEIVED {
        @Override
        public String getLabel() {
            return "Recibida";
        }

        @Override
        public String getDescription() {
            return "La solicitud ha sido recibida correctamente y está en espera de ser revisada.";
        }

        @Override
        public boolean isFinal() {
            return false;
        }
    }, UNDER_REVIEW {
        @Override
        public String getLabel() {
            return "En revisión";
        }

        @Override
        public String getDescription() {
            return "La solicitud está siendo evaluada por el personal encargado para determinar cómo proceder.";
        }

        @Override
        public boolean isFinal() {
            return false;
        }
    }, IN_PROGRESS {
        @Override
        public String getLabel() {
            return "En progreso";
        }

        @Override
        public String getDescription() {
            return "La solicitud ha sido aceptada y se están tomando medidas para atenderla o resolverla.";
        }

        @Override
        public boolean isFinal() {
            return false;
        }
    }, PENDING_INFORMATION {
        @Override
        public String getLabel() {
            return "Pendiente de información";
        }

        @Override
        public String getDescription() {
            return "Se ha solicitado información adicional al solicitante para poder continuar con la revisión o resolución de la solicitud.";
        }

        @Override
        public boolean isFinal() {
            return false;
        }
    }, RESOLVED {
        @Override
        public String getLabel() {
            return "Resuelta";
        }

        @Override
        public String getDescription() {
            return "La solicitud ha sido atendida y se ha proporcionado una solución o respuesta final.";
        }

        @Override
        public boolean isFinal() {
            return true;
        }
    }, CLOSED {
        @Override
        public String getLabel() {
            return "Cerrada";
        }

        @Override
        public String getDescription() {
            return "La solicitud ha sido cerrada, ya sea porque fue resuelta o porque no se requiere más acción.";
        }

        @Override
        public boolean isFinal() {
            return true;
        }
    }, REJECTED {
        @Override
        public String getLabel() {
            return "Rechazada";
        }

        @Override
        public String getDescription() {
            return "La solicitud fue evaluada, pero no se procederá con ella, ya sea por falta de fundamento, por no estar dentro del ámbito de acción de la institución, o por incumplir los requisitos.";
        }

        @Override
        public boolean isFinal() {
            return true;
        }
    }, CANCELLED {
        @Override
        public String getLabel() {
            return "Cancelada";
        }

        @Override
        public String getDescription() {
            return "El solicitante decidió retirar la solicitud antes de que fuera resuelta.";
        }

        @Override
        public boolean isFinal() {
            return true;
        }
    };

    public abstract String getLabel();

    public abstract String getDescription();

    public abstract boolean isFinal();
}
