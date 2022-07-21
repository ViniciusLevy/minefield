package br.com.minefield.model;

import br.com.minefield.exception.ExplosionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {
    private Field field;

    @BeforeEach
    void initializeField() {
        field = new Field(3,3);
    }

    @Test
    void neighborTestDistance1Left() {
        Field neighbor = new Field(3, 2);
        boolean result = field.addNeighbor(neighbor);
        assertTrue(result);
    }

    @Test
    void neighborTestDistance1Right() {
        Field neighbor = new Field(3, 4);
        boolean result = field.addNeighbor(neighbor);
        assertTrue(result);
    }

    @Test
    void neighborTestDistance1Upper() {
        Field neighbor = new Field(2, 3);
        boolean result = field.addNeighbor(neighbor);
        assertTrue(result);
    }

    @Test
    void neighborTestDistance1Lower() {
        Field neighbor = new Field(4, 3);
        boolean result = field.addNeighbor(neighbor);
        assertTrue(result);
    }

    @Test
    void neighborTestDistance2Diagonal() {
        Field neighbor = new Field(2, 2);
        boolean result = field.addNeighbor(neighbor);
        assertTrue(result);
    }

    @Test
    void notNeighborTest() {
        Field neighbor = new Field(1, 1);
        boolean result = field.addNeighbor(neighbor);
        assertFalse(result);
    }

    @Test
    void defaultMarkerTest() {
        assertFalse(field.isMarked());
    }

    @Test
    void switchMarkerTest() {
        field.switchMarker();
        assertTrue(field.isMarked());
    }

    @Test
    void switchMarkerTestTwoTimes() {
        field.switchMarker();
        field.switchMarker();
        assertFalse(field.isMarked());
    }

    @Test
    void openWithoutMineOrMarkerTest() {
        assertTrue(field.open());
    }

    @Test
    void openWithoutMineWithMarkerTest() {
        field.switchMarker();
        assertFalse(field.open());
    }

    @Test
    void openMineWithMarkerTest() {
        field.switchMarker();
        field.addMine();
        assertFalse(field.open());
    }

    @Test
    void openMineWithoutMarkerTest() {
        field.addMine();
        assertThrows(ExplosionException.class, () -> {
            field.open();
        });
    }

    @Test
    void openWithNeighborsTest1() {
        Field neighbor11 = new Field(1,1);
        Field neighbor22 = new Field(2, 2);
        neighbor22.addNeighbor(neighbor11);

        field.addNeighbor(neighbor22);
        field.open();

        assertTrue(neighbor22.isOpened() && neighbor11.isOpened());
    }

    @Test
    void openWithNeighborsTest2() {
        Field neighbor11 = new Field(1,1);
        Field neighbor12 = new Field(1,2);
        neighbor12.addMine();

        Field neighbor22 = new Field(2, 2);
        neighbor22.addNeighbor(neighbor11);
        neighbor22.addNeighbor(neighbor12);

        field.addNeighbor(neighbor22);
        field.open();

        assertTrue(neighbor22.isOpened() && neighbor11.isClosed());
    }
}
