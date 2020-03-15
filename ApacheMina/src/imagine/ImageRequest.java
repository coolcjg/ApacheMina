package imagine;

public class ImageRequest {
	
	private int width;
	private int height;
	private int numberOfCharacters;
	
	public ImageRequest(int width, int height, int numberOfCharacters) {
		this.width = width;
		this.height = height;
		this.numberOfCharacters = numberOfCharacters;
	}
	
	public int getWith() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getNumberOfCharacters() {
		return numberOfCharacters;
	}
	

}
