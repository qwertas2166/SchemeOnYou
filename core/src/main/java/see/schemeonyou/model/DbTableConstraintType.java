package see.schemeonyou.model;

public enum DbTableConstraintType {
    COMPOSITE_PRIMARY_KEY("composite_primary_key"),
    COMPOSITE_UNIQUE("composite_unique");

    private final String storageName;

    DbTableConstraintType(String storageName) {
        this.storageName = storageName;
    }

    public String storageName() {
        return storageName;
    }

    public static DbTableConstraintType fromStorageName(String value, String field) {
        for (DbTableConstraintType type : values()) {
            if (type.storageName.equals(value)) return type;
        }
        throw new IllegalArgumentException("Invalid table constraint type at " + field + ": " + value);
    }
}
