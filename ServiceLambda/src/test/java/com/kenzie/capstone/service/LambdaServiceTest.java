package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.NonCachingIngredientsDao;
import com.kenzie.capstone.service.dao.NonCachingIntoleranceDao;
import com.kenzie.capstone.service.dao.RecipeDao;

import com.kenzie.capstone.service.model.IngredientsData;
import com.kenzie.capstone.service.model.IngredientsRecord;
import com.kenzie.capstone.service.model.IntoleranceData;
import com.kenzie.capstone.service.model.IntoleranceRecord;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaServiceTest {

    /** ------------------------------------------------------------------------
     *  expenseService.getExpenseById
     *  ------------------------------------------------------------------------ **/

    private NonCachingIngredientsDao ingredientsDao;
    private NonCachingIntoleranceDao intoleranceDao;
    private RecipeDao recipeDao;
    private LambdaService lambdaService;

    @BeforeAll
    void setup() {
        this.recipeDao = mock(RecipeDao.class);
        this.ingredientsDao = mock(NonCachingIngredientsDao.class);
        this.intoleranceDao = mock(NonCachingIntoleranceDao.class);
        this.lambdaService = new LambdaService(ingredientsDao, intoleranceDao, recipeDao);
    }

    @Test
    void setIngredientsDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> dataCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String data = "somedata";

        // WHEN
        IngredientsData response = this.lambdaService.setIngredientsData(data);

        // THEN
        verify(ingredientsDao, times(1)).setIngredientsData(idCaptor.capture(), dataCaptor.capture());

        assertNotNull(idCaptor.getValue(), "An ID is generated");
        assertEquals(data, dataCaptor.getValue(), "The data is saved");

        assertNotNull(response, "A response is returned");
        assertEquals(idCaptor.getValue(), response.getId(), "The response id should match");
        assertEquals(data, response.getData(), "The response data should match");
    }

    @Test
    void getIngredientsDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String id = "fakeid";
        String data = "somedata";
        IngredientsRecord record = new IngredientsRecord();
        record.setId(id);
        record.setData(data);


        when(ingredientsDao.getIngredientsData(id)).thenReturn(Arrays.asList(record));

        // WHEN
        IngredientsData response = this.lambdaService.getIngredientsData(id);

        // THEN
        verify(ingredientsDao, times(1)).getIngredientsData(idCaptor.capture());

        assertEquals(id, idCaptor.getValue(), "The correct id is used");

        assertNotNull(response, "A response is returned");
        assertEquals(id, response.getId(), "The response id should match");
        assertEquals(data, response.getData(), "The response data should match");
    }

    @Test
    void setIntoleranceDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> dataCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String data = "somedata";

        // WHEN
        IntoleranceData response = this.lambdaService.setIntoleranceData(data);

        // THEN
        verify(intoleranceDao, times(1)).setIntoleranceData(idCaptor.capture(), dataCaptor.capture());

        assertNotNull(idCaptor.getValue(), "An ID is generated");
        assertEquals(data, dataCaptor.getValue(), "The data is saved");

        assertNotNull(response, "A response is returned");
        assertEquals(idCaptor.getValue(), response.getId(), "The response id should match");
        assertEquals(data, response.getData(), "The response data should match");
    }

    @Test
    void getIntoleranceDataTest() {
        ArgumentCaptor<String> idCaptor = ArgumentCaptor.forClass(String.class);

        // GIVEN
        String id = "fakeid";
        String data = "somedata";
        IntoleranceRecord record = new IntoleranceRecord();
        record.setId(id);
        record.setData(data);


        when(intoleranceDao.getIntoleranceData(id)).thenReturn(Arrays.asList(record));

        // WHEN
        IntoleranceData response = this.lambdaService.getIntoleranceData(id);

        // THEN
        verify(intoleranceDao, times(1)).getIntoleranceData(idCaptor.capture());

        assertEquals(id, idCaptor.getValue(), "The correct id is used");

        assertNotNull(response, "A response is returned");
        assertEquals(id, response.getId(), "The response id should match");
        assertEquals(data, response.getData(), "The response data should match");
    }

    // Write additional tests here

}