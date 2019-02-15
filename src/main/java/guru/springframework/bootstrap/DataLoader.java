package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Optional;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final RecipeRepository recipeRepository;
    private final ResourceLoader resourceLoader;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    public DataLoader(RecipeRepository recipeRepository, ResourceLoader resourceLoader, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.resourceLoader = resourceLoader;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    public void createRecipes() throws Exception {
        Recipe guacamole = new Recipe();
        guacamole.setCookTime(10);
        guacamole.setPrepTime(10);
        guacamole.setDifficulty(Difficulty.EASY);
        guacamole.setServings(4);
        guacamole.setSource("Simply Recipes");
        guacamole.setDescription("The BEST guacamole! So easy to make with ripe avocados, salt, serrano chiles, cilantro and lime. Garnish with red radishes or jicama. Serve with tortilla chips. Watch how to make guacamole - it's easy!");
        guacamole.setDirections("Be careful handling chiles if using. Wash your hands thoroughly after handling and do not touch your eyes or the area near your eyes with your hands for several hours.");
        guacamole.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        Notes guacamoleNotes = new Notes();
        guacamoleNotes.setRecipeNotes("1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n" +
                "\n" +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
                "\n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "\n" +
                "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.\n" +
                "\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n" +
                "\n" +
                "Variations\n" +
                "\n" +
                "For a very quick guacamole just take a 1/4 cup of salsa and mix it in with your mashed avocados.\n" +
                "\n" +
                "Feel free to experiment! One classic Mexican guacamole has pomegranate seeds and chunks of peaches in it (a Diana Kennedy favorite). Try guacamole with added pineapple, mango, or strawberries (see our Strawberry Guacamole).\n" +
                "\n" +
                "The simplest version of guacamole is just mashed avocados with salt. Don't let the lack of availability of other ingredients stop you from making guacamole.\n" +
                "\n" +
                "To extend a limited supply of avocados, add either sour cream or cottage cheese to your guacamole dip. Purists may be horrified, but so what? It tastes great.");
        guacamole.setNotes(guacamoleNotes);
        File file = resourceLoader.getResource("classpath:devData/images/guacamole.jpg").getFile();
        guacamole.setImage(ArrayUtils.toObject(Files.readAllBytes(file.toPath())));
        Ingredient avocados = new Ingredient();
        avocados.setAmount(BigDecimal.valueOf(2));
        avocados.setDescription("Avocado");
        Optional<UnitOfMeasure> uom = unitOfMeasureRepository.findByDescription("Item");
        avocados.setUom(uom.orElseThrow(() -> new RuntimeException("Unit of Measure 'Item' not found.")));
        guacamole.getIngredients().add(avocados);
        recipeRepository.save(guacamole);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            createRecipes();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
