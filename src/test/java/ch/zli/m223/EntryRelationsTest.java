package ch.zli.m223;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

@QuarkusTest
public class EntryRelationsTest {

    private Long create(String path, String json) {
        return given()
          .contentType("application/json")
          .body(json)
          .when().post(path)
          .then()
             .statusCode(200)
             .extract().jsonPath().getLong("id");
    }

    @Test
    public void testCategoryAndTagEndpoints() {
        Long categoryId = create("/categories", """
            {"title":"Projekt"}
            """);

        given()
          .when().get("/categories/" + categoryId)
          .then()
             .statusCode(200)
             .body("title", is("Projekt"));

        given()
          .contentType("application/json")
          .body("""
              {"title":"Schule"}
              """)
          .when().put("/categories/" + categoryId)
          .then()
             .statusCode(200)
             .body("title", is("Schule"));

        // Titel fehlt -> 400
        given()
          .contentType("application/json")
          .body("{}")
          .when().post("/tags")
          .then()
             .statusCode(400);

        given()
          .when().get("/tags/999999")
          .then()
             .statusCode(404);

        given()
          .when().delete("/categories/" + categoryId)
          .then()
             .statusCode(204);

        given()
          .when().get("/categories/" + categoryId)
          .then()
             .statusCode(404);
    }

    @Test
    public void testEntryWithCategoryAndTags() {
        Long categoryId = create("/categories", """
            {"title":"Projekt"}
            """);
        Long meetingId = create("/tags", """
            {"title":"Meeting"}
            """);
        Long remoteId = create("/tags", """
            {"title":"Remote"}
            """);

        Long entryId = given()
          .contentType("application/json")
          .body("""
              {"checkIn":"2026-10-27T08:00:00","checkOut":"2026-10-27T12:00:00",
               "categoryId":%d,"tagIds":[%d,%d]}
              """.formatted(categoryId, meetingId, remoteId))
          .when().post("/entries")
          .then()
             .statusCode(200)
             .body("categoryId", is(categoryId.intValue()))
             .body("tagIds.size()", is(2))
             .extract().jsonPath().getLong("id");

        // Entry ohne Kategorie und ohne Tags
        Long plainId = create("/entries", """
            {"checkIn":"2026-10-27T13:00:00","checkOut":"2026-10-27T17:00:00"}
            """);

        given()
          .when().get("/entry-details/" + entryId)
          .then()
             .statusCode(200)
             .body("category.id", is(categoryId.intValue()))
             .body("category.title", is("Projekt"))
             .body("tags.size()", is(2))
             .body("tags[0].title", is("Meeting"))
             .body("tags[1].title", is("Remote"));

        given()
          .when().get("/entry-details/" + plainId)
          .then()
             .statusCode(200)
             .body("category", nullValue())
             .body("tags.size()", is(0));

        given()
          .when().get("/entry-details")
          .then()
             .statusCode(200)
             .body("size()", is(2));

        given()
          .when().get("/entry-details/999999")
          .then()
             .statusCode(404);

        // Unbekannte IDs -> 404
        given()
          .contentType("application/json")
          .body("""
              {"checkIn":"2026-10-27T08:00:00","checkOut":"2026-10-27T12:00:00","categoryId":999999}
              """)
          .when().post("/entries")
          .then()
             .statusCode(404);

        given()
          .contentType("application/json")
          .body("""
              {"checkIn":"2026-10-27T08:00:00","checkOut":"2026-10-27T12:00:00","tagIds":[999999]}
              """)
          .when().put("/entries/" + entryId)
          .then()
             .statusCode(404);

        // Beim Bearbeiten nur noch ein Tag, keine Kategorie
        given()
          .contentType("application/json")
          .body("""
              {"checkIn":"2026-10-27T08:00:00","checkOut":"2026-10-27T12:00:00","tagIds":[%d]}
              """.formatted(remoteId))
          .when().put("/entries/" + entryId)
          .then()
             .statusCode(200)
             .body("categoryId", nullValue())
             .body("tagIds.size()", is(1));

        // Tag löschen entfernt ihn auch beim Entry
        given()
          .when().delete("/tags/" + remoteId)
          .then()
             .statusCode(204);

        given()
          .when().get("/entry-details/" + entryId)
          .then()
             .statusCode(200)
             .body("tags.size()", is(0));

        // Aufräumen, damit die anderen Tests mit einer leeren Datenbank starten
        given().when().delete("/entries/" + entryId).then().statusCode(204);
        given().when().delete("/entries/" + plainId).then().statusCode(204);
        given().when().delete("/tags/" + meetingId).then().statusCode(204);
        given().when().delete("/categories/" + categoryId).then().statusCode(204);
    }
}
