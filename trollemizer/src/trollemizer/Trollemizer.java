package trollemizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

public class Trollemizer {
	public static final int version = 1;
	private String outputDirectory;
	private String inputLocation;
	private File inputFile;
	private RandomAccessFile outputRom;
	private byte[] outputBuffer;
	private int seed;
	private Boolean outputSpoiler = false;
	private Random random;
	private String outputName;
	
	public Trollemizer() {
		random = new Random();
	}
	
	public void randomize(int seed, String inputRomDirectory, String outputDirectory, String outputName, Boolean randomizeTRPegs, Boolean randomizeItemGFX) {
		setSeed(seed);
		loadRom(inputRomDirectory);
		setRomDirectory(outputDirectory);
		setOutputName(outputName);
		
		if (randomizeTRPegs) {
			this.randomizePegs();
		}
		
		if (randomizeItemGFX) {
			this.randomizeGFX();
		}
		
		writeRom();
	}
	
	public Boolean loadRom(String location) {
		inputLocation = location;
		inputFile = new File(location);
		
		if (!inputFile.exists()) {
			System.out.println("Could not find file: " +  location);
			return false;
		}
		
		try {
			outputBuffer = Files.readAllBytes(Path.of(location));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void setRomDirectory(String location) {
		this.outputDirectory = location;
	}
	
	public void setOutputName(String name) {
		this.outputName = name;
	}
	
	public void setSeed(int newSeed) {
		if (newSeed < 0) {
			newSeed *= -1;
		}
		
		this.seed = newSeed;
		random.setSeed(this.seed);
	}
	
	public void outputSpoiler() {
		//TODO - Make spoilers actually output
	}
	
	public void writeRom() {
		String filePath = this.outputDirectory + "\\" + this.outputName + " " + this.inputFile.getName();
		File outputRomFile = new File(filePath);
		
		if (!outputRomFile.exists()) {
			try {
				outputRomFile.createNewFile();
			} catch (IOException e) {
				System.out.println("Failed to create " + filePath);
			}
		}
		
		try {
			outputRom = new RandomAccessFile(outputRomFile, "rw");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			outputRom.seek(0);
			outputRom.write(outputBuffer);
			outputRom.close();
		} catch (IOException e) {
			System.out.println("Failed to write to " + filePath);
		}
	}
	
	public void randomizePegs() {
		int[] pegOrder = {0,1,2};
		String[] pegSpoiler = {"Right", "Up", "Left"};
		byte[][] pegValues = {{0x26, 0x08}, {(byte) 0xA0, 0x05}, {0x1A, 0x08}};
		
		ArrayUtils.shuffle(pegOrder, random);
		for (int i = 0; i < pegOrder.length; i++) {
			writeOffset(pegValues[pegOrder[i]], 0x267A1 + 2*i);
		}
		
		//TODO - Move to outputSpoiler, write to file instead
		if (outputSpoiler) {
			System.out.println("Peg order: " + pegSpoiler[pegOrder[0]] + " " + pegSpoiler[pegOrder[1]] + " " + pegSpoiler[pegOrder[2]]);
		}
	}
	
	public void randomizeGFX() {
		byte[] gfxSearchValues = {0x06, 0x18, 0x18, 0x18, 0x2D, 0x20, 0x2E, 0x09};
		byte[] sizeSearchValues = {0x00, 0x00, 0x00, 0x00, 0x00, 0x02, 0x02, 0x00};
		byte[] spriteGFXSearchValues = {0x06, 0x44, 0x45, 0x46, 0x2D, 0x20, 0x2E, 0x09};
		byte[] spritePaletteSearchValues = {0x00, 0x04, 0x02, 0x08, 0x04, 0x02, 0x08, 0x02};
		byte[] yOffsetSearchValues = {-5, -5, -5, -5, -5, -4, -4, -5};
		byte[] xOffsetSearchValues = {4,  4,  4,  4,  4,  0,  0,  4};
		
		int gfxOffset = searchRom(gfxSearchValues, 0x100000, -1);
		int sizeOffset = searchRom(sizeSearchValues, 0x100000, -1);
		int spriteGFXOffset = searchRom(spriteGFXSearchValues, 0x100000, -1);
		int spritePaletteOffset = searchRom(spritePaletteSearchValues, 0x100000, -1);
		int yOffset = searchRom(yOffsetSearchValues, 0x100000, -1);
		int xOffset = searchRom(xOffsetSearchValues, 0x100000, -1);
		
		byte[] gfxTable = readOffset(gfxOffset, 0xB0);
		byte[] dynamicGFXTable = gfxTable.clone();
		byte[] sizeTable = readOffset(sizeOffset, 0xB0);
		byte[] dynamicSizeTable = sizeTable.clone();
		byte[] spriteGFXTable = readOffset(spriteGFXOffset, 0xB0);
		byte[] dynamicSpriteGFXTable = spriteGFXTable.clone();
		byte[] spritePaletteTable = readOffset(spritePaletteOffset, 0xB0);
		byte[] dynamicSpritePaletteTable = spritePaletteTable.clone();
		byte[] yTable = readOffset(yOffset, 0xB0);
		byte[] dynamicYTable = yTable.clone();
		byte[] xTable = readOffset(xOffset, 0xB0);
		byte[] dynamicXTable = xTable.clone();
		
		for (int i = 0; i < 0xB0; i++) {
			Byte currentGFX = gfxTable[i];
			
			if (currentGFX.equals((byte) 0x00) || currentGFX.equals((byte) 0xFF)) {
				continue;
			}
			
			int tableSize = dynamicGFXTable.length;
			int randomValue = 0;
			Byte gfxValue = 0;
			
			do {
				randomValue = random.nextInt(tableSize);
				gfxValue = dynamicGFXTable[randomValue];
			} while (gfxValue.equals((byte) 0x00) || gfxValue.equals((byte) 0xFF));
			
			writeOffset(gfxValue, gfxOffset + i);
			writeOffset(dynamicSizeTable[randomValue], sizeOffset + i);
			writeOffset(dynamicSpriteGFXTable[randomValue], spriteGFXOffset + i);
			writeOffset(dynamicSpritePaletteTable[randomValue], spritePaletteOffset + i);
			writeOffset(dynamicYTable[randomValue], yOffset + i);
			writeOffset(dynamicXTable[randomValue], xOffset + i);
			
			dynamicGFXTable = ArrayUtils.remove(dynamicGFXTable, randomValue);
			dynamicSizeTable = ArrayUtils.remove(dynamicSizeTable, randomValue);
			dynamicSpritePaletteTable = ArrayUtils.remove(dynamicSpritePaletteTable, randomValue);
			dynamicYTable = ArrayUtils.remove(dynamicYTable, randomValue);
			dynamicXTable = ArrayUtils.remove(dynamicXTable, randomValue);
		}
	}
	
	private void writeOffset(byte value, int offset) {
		outputBuffer[offset] = value;
	}
	
	private void writeOffset(byte[] values, int offset) {
		for (int i = 0; i < values.length; i++) {
			outputBuffer[offset + i] = values[i];
		}
	}
	
	private byte[] readOffset(int offset, int length) {
		byte[] readBuffer = new byte[length];
		
		for (int i = 0; i < length; i++) {
			readBuffer[i] = outputBuffer[offset + i];
		}
		
		return readBuffer;
	}
	
	private int searchRom(byte[] searchValues) {
		int offset = 0;
		
		while (offset < outputBuffer.length) {
			for (int i = 0; i < searchValues.length; i++) {
				if (searchValues[i] == outputBuffer[offset + i]) {
					if (i == searchValues.length - 1) {
						return offset;
					}
					continue;
				} else {
					break;
				}
			}
		}
		
		return -1;
	}
	
	private int searchRom(byte[] searchValues, int searchStart, int searchEnd) {
		if (searchEnd == -1) {
			searchEnd = outputBuffer.length;
		}
		if (searchStart > searchEnd) {
			return -1;
		}
		
		int offset = searchStart;
		
		while (offset < searchEnd) {
			for (int i = 0; i < searchValues.length; i++) {
				if (searchValues[i] == outputBuffer[offset + i]) {
					if (i == searchValues.length - 1) {
						return offset;
					}
					continue;
				} else {
					break;
				}
			}
			offset++;
		}
		
		return -1;
	}
	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Args:\n    input_file - An A Link to the Past randomized rom\n    output_directory - The directory to write the modified rom to\n    --spoiler - If present, a spoiler log will be outputted\n    --seed (seed) - If present, specifies the seed to be used for generation\n\\n    --pegs - If present, will randomize the turtle rock pegs\n    --gfx - If present, will randomize the item GFX");
			return;
		}
		
		String sourceFileLocation = "";
		String outputFileDirectory = "";
		Boolean spoiler = false;
		Boolean customSeed = false;
		Boolean randomizePegs = false;
		Boolean randomizeGFX = false;
		int seed = 0;
		
		sourceFileLocation = args[0];
		outputFileDirectory = args[1];
		
		for (int i = 2; i < args.length; i++) {
			switch (args[i]) {
				case "--spoiler":
					spoiler = true;
					break;
				case "--seed":
					customSeed = true;
					seed = Integer.parseInt(args[++i]);
					break;
				case "--pegs":
					randomizePegs = true;
					break;
				case "--gfx":
					randomizeGFX = true;
					break;
			}
		}
		
		if (!customSeed) {
			seed = new Random().nextInt();
		}
		
		Trollemizer randomizer = new Trollemizer();

		randomizer.randomize(seed, sourceFileLocation, outputFileDirectory, "Trollemized_" + seed + " -", randomizePegs, randomizeGFX);
    }
}
