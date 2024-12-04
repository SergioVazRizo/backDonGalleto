package dongalleto.model;

import java.util.List;

public class Recipes {

    private int recipe_id;
    private String recipe_name;
    private int recipe_yield;
    private List<String> instructions;

    // Atributos para manejar las listas de ingredientes
    private List<Integer> ingredient_ids;
    private List<Double> ingredient_quantities;
    private List<String> ingredient_names;
    private List<Double> ingredient_stocks;
    private List<String> ingredient_units;

    // Constructor sin parámetros
    public Recipes() {
    }

    // Constructor con parámetros
    public Recipes(int recipe_id, String recipe_name, int recipe_yield, List<String> instructions,
                   List<Integer> ingredient_ids, List<Double> ingredient_quantities, 
                   List<String> ingredient_names, List<Double> ingredient_stocks, List<String> ingredient_units) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.recipe_yield = recipe_yield;
        this.instructions = instructions;
        this.ingredient_ids = ingredient_ids;
        this.ingredient_quantities = ingredient_quantities;
        this.ingredient_names = ingredient_names;
        this.ingredient_stocks = ingredient_stocks;
        this.ingredient_units = ingredient_units;
    }

    // Getters y Setters
    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public int getRecipe_yield() {
        return recipe_yield;
    }

    public void setRecipe_yield(int recipe_yield) {
        this.recipe_yield = recipe_yield;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    // Métodos para los ingredientes
    public List<Integer> getIngredient_ids() {
        return ingredient_ids;
    }

    public void setIngredient_ids(List<Integer> ingredient_ids) {
        this.ingredient_ids = ingredient_ids;
    }

    public List<Double> getIngredient_quantities() {
        return ingredient_quantities;
    }

    public void setIngredient_quantities(List<Double> ingredient_quantities) {
        this.ingredient_quantities = ingredient_quantities;
    }

    public List<String> getIngredient_names() {
        return ingredient_names;
    }

    public void setIngredient_names(List<String> ingredient_names) {
        this.ingredient_names = ingredient_names;
    }

    public List<Double> getIngredient_stocks() {
        return ingredient_stocks;
    }

    public void setIngredient_stocks(List<Double> ingredient_stocks) {
        this.ingredient_stocks = ingredient_stocks;
    }

    public List<String> getIngredient_units() {
        return ingredient_units;
    }

    public void setIngredient_units(List<String> ingredient_units) {
        this.ingredient_units = ingredient_units;
    }

    // Método toString para representar el objeto como una cadena
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Recipes{");
        sb.append("recipe_id:").append(recipe_id);
        sb.append(", recipe_name:").append(recipe_name);
        sb.append(", recipe_yield:").append(recipe_yield);
        sb.append(", instructions:").append(instructions);
        sb.append(", ingredient_ids:").append(ingredient_ids);
        sb.append(", ingredient_quantities:").append(ingredient_quantities);
        sb.append(", ingredient_names:").append(ingredient_names);
        sb.append(", ingredient_stocks:").append(ingredient_stocks);
        sb.append(", ingredient_units:").append(ingredient_units);
        sb.append('}');
        return sb.toString();
    }
}