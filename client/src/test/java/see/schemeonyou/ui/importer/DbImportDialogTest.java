package see.schemeonyou.ui.importer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DbImportDialogTest {
    @Test
    void formatsSingleImportWarningSimplyAndRedactsSecret() {
        String status = DbImportDialog.formatImportSuccessStatus(
                2,
                1,
                List.of("Skipped constraint that mentioned secret-password"),
                "secret-password"
        );

        assertEquals("Done: imported 2 tables and 1 foreign keys. Use Ctrl+Z to undo. Warning: Skipped constraint that mentioned ******", status);
        assertFalse(status.contains("secret-password"));
    }

    @Test
    void formatsAllImportWarningsWithCountAndDetails() {
        String status = DbImportDialog.formatImportSuccessStatus(
                3,
                2,
                List.of("Skipped composite FK fk_order_line", "Skipped unsupported metadata on payments"),
                "unused"
        );

        assertTrue(status.startsWith("Done: imported 3 tables and 2 foreign keys. Use Ctrl+Z to undo. Warnings (2):"));
        assertTrue(status.contains("- Skipped composite FK fk_order_line"));
        assertTrue(status.contains("- Skipped unsupported metadata on payments"));
    }

    @Test
    void closesAutomaticallyOnlyWhenThereAreNoWarningsMessageRemainsUnchanged() {
        assertEquals(
                "Done: imported 1 tables and 0 foreign keys. Use Ctrl+Z to undo.",
                DbImportDialog.formatImportSuccessStatus(1, 0, List.of(), "secret")
        );
    }
}
