/*************************************************************************
 *  Compilation:  javac ArtCollage.java
 *  Execution:    java ArtCollage Flo2.jpeg
 *
 *  @author:
 *
 *************************************************************************/

import java.awt.Color;

public class ArtCollage {

    // The orginal picture
    private Picture original;

    // The collage picture
    private Picture collage;

    // The collage Picture consists of collageDimension X collageDimension tiles
    private int collageDimension;

    // A tile consists of tileDimension X tileDimension pixels
    private int tileDimension;
    
    /*
     * One-argument Constructor
     * 1. set default values of collageDimension to 4 and tileDimension to 100
     * 2. initializes original with the filename image
     * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension, 
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage (String filename) {
        collageDimension = 4;
        tileDimension = 100;

        int num = collageDimension * tileDimension;

        original = new Picture(filename);
        collage = new Picture(num, num);

        for(int i=0; i<num; i++) {
            for(int r=0; r<num; r++) {
                int mcol = i * original.width()/num;
                int mrow = r * original.height()/num;
                Color color = original.get(mcol, mrow);
                collage.set(i, r, color);

            }
        }
	    
    }

    /*
     * Three-arguments Constructor
     * 1. set default values of collageDimension to cd and tileDimension to td
     * 2. initializes original with the filename image
     * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension, 
     *    where each pixel is black (see all constructors for the Picture class).
     * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage (String filename, int td, int cd) {

	    collageDimension = cd;
        tileDimension = td;
        int num = collageDimension * tileDimension;

        original = new Picture(filename);
        collage = new Picture(num, num);

        for(int col = 0; col<collage.width();col++){
            for(int row = 0; row <collage.height(); row++) {
                Color color = collage.get(col, row);
                Color gray = Luminance.toGray(color);
                collage.set(col, row, gray);
            }
        }

        for(int i =0; i<num; i++) {
            for(int r=0; r<num; r++){
                int mcol = i *original.width()/num;
                int mrow = r *original.height()/num;
                Color color = original.get(mcol, mrow);
                collage.set(i, r, color);
            }
        }
    }

    /*
     * Returns the collageDimension instance variable
     *
     * @return collageDimension
     */
    public int getCollageDimension() {

	    return collageDimension;
    }

    /*
     * Returns the tileDimension instance variable
     *
     * @return tileDimension
     */
    public int getTileDimension() {

	    return getTileDimension();
    }

    /*
     * Returns original instance variable
     *
     * @return original
     */
    public Picture getOriginalPicture() {

	    return original;
    }

    /*
     * Returns collage instance variable
     *
     * @return collage
     */
    public Picture getCollagePicture() {

	    return collage;
    }
    
    /*
     * Display the original image
     * Assumes that original has been initialized
     */
    public void showOriginalPicture() {

	    original.show();
    }

    /*
     * Display the collage image
     * Assumes that collage has been initialized
     */
    public void showCollagePicture() {

	    collage.show();
    }

    /*
     * Replaces the tile at collageCol,collageRow with the image from filename
     * Tile (0,0) is the upper leftmost tile
     *
     * @param filename image to replace tile
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void replaceTile (String filename,  int collageCol, int collageRow) {

	    Picture rep = new Picture(filename);
        for (int tcol = 0; tcol < tileDimension; tcol++) {
            for (int trow = 0; trow < tileDimension; trow++) {
                int scol = tcol * rep.width() / (tileDimension);
                int srow = trow * rep.height() / (tileDimension);
                Color color = rep.get(scol, srow);

                collage.set(tcol + (collageCol * tileDimension), trow + (collageRow * tileDimension), color);
    }
}
    }
    /*
     * Makes a collage of tiles from original Picture
     * original will have collageDimension x collageDimension tiles, each tile
     * has tileDimension X tileDimension pixels
     */
    public void makeCollage () {
        int width = tileDimension * collageDimension;
        int height = tileDimension * collageDimension;
        this.collage = new Picture(width, height);

        for (int col = 0; col < tileDimension; col++) {
            for (int row = 0; row < tileDimension; row++) {
                int k = col * this.original.width() / tileDimension;
                int l = row * this.original.height() / tileDimension;
                Color color = this.original.get(k, l);
                for (int i = 0; i < collageDimension; i++) {
                    for (int j = 0; j < collageDimension; j++) {
                        this.collage.set(i * tileDimension + col, j * tileDimension + row, color);
                    }
                }
            }
        }
    }

    /*
     * Colorizes the tile at (collageCol, collageRow) with component 
     * (see CS111 Week 9 slides, the code for color separation is at the 
     *  book's website)
     *
     * @param component is either red, blue or green
     * @param collageCol tile column
     * @param collageRow tile row
     */
    public void colorizeTile (String component,  int collageCol, int collageRow) {
        for (int i = 0; i < tileDimension; i++) {
            for (int j = 0; j < tileDimension; j++) {
                Color color = collage.get(i + (collageCol * tileDimension), j + (collageRow * tileDimension));
                if (component.equalsIgnoreCase("red")) {
                    int r = color.getRed();
                    Color red = new Color(r, 0, 0);
                    collage.set(i + (collageCol * tileDimension), j + (collageRow * tileDimension), red);

                } else if (component.equalsIgnoreCase("blue")) {
                    int b = color.getBlue();
                    Color blue = new Color(0, 0, b);
                    collage.set(i + (collageCol * tileDimension), j + (collageRow * tileDimension), blue);

                } else if (component.equalsIgnoreCase("green")) {
                    int g = color.getGreen();
                    Color green = new Color(0, g, 0);
                    collage.set(i + (collageCol * tileDimension), j + (collageRow * tileDimension), green);
                    
                }
            }
        }
    }

    /*
     * Grayscale tile at (collageCol, collageRow)
     * (see CS111 Week 9 slides, the code for luminance is at the book's website)
     *
     * @param collageCol tile column
     * @param collageRow tile row
     */

    public void grayscaleTile (int collageCol, int collageRow) {
        for (int i = 0; i < tileDimension; i++) {
            for (int j = 0; j < tileDimension; j++) {
                Color color = collage.get(i + (collageCol * tileDimension), j + (collageRow * tileDimension));
                Color gray = Luminance.toGray(color);
                collage.set(i + (collageCol * tileDimension), j + (collageRow * tileDimension), gray);
            }
        }
    }


    /*
     *
     *  Test client: use the examples given on the assignment description to test your ArtCollage
     */
    public static void main (String[] args) {

        ArtCollage art = new ArtCollage(args[0], 200, 3); 
        art.makeCollage();
        art.colorizeTile("red",2,2);
        art.showCollagePicture();
    }
}


