package net.singularity.text;

import java.util.HashMap;
import java.util.Map;

public class Font {
    private final Map<Character, Glyph> glyphs;

    public Font() {
        this.glyphs = new HashMap<>();
        init();
    }

    private void init() {
        for(int i = 0; i < 256; i++) {
            char c = (char)i;
            Glyph ch = new Glyph(8, 8, i % 32, i / 32);
            glyphs.put(c, ch);
        }
    }

    public Glyph getGlyph(char c) {
        return glyphs.get(c);
    }
}
