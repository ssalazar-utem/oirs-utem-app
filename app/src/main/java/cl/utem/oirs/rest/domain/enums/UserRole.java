package cl.utem.oirs.rest.domain.enums;

public enum UserRole {
    STUDENT {
        @Override
        public String getLabel() {
            return "Estudiante";
        }
    }, STAFF {
        @Override
        public String getLabel() {
            return "Personal";
        }
    }, ACADEMIC {
        @Override
        public String getLabel() {
            return "Académico";
        }
    };

    public abstract String getLabel();
}
