package cl.utem.oirs.rest.domain.enums;

/**
 *
 * @author seba
 */
public enum IcsoType {
    CLAIM {
        @Override
        public String getLabel() {
            return "Reclamo";
        }
    }, SUGGESTION {
        @Override
        public String getLabel() {
            return "Sugerencia";
        }
    }, INFORMATION {
        @Override
        public String getLabel() {
            return "Información";
        }
    };

    public abstract String getLabel();
}
