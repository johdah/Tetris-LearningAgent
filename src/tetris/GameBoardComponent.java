package tetris;

import java.awt.*;
import java.util.Hashtable;

/**
 * The graphical component that paints the square board. This is
 *  implemented as an inner class in order to better abstract the
 *  detailed information that must be sent between the square board
 *  and its graphical representation.
 *
 * User: Johan
 * Date: 2013-03-14
 * Time: 15:21
 */
public class GameBoardComponent extends Component {
    private GameBoard gameBoard;

    /**
     * The component size. If the component has been resized, that
     * will be detected when the paint method executes. If this
     * value is set to null, the component dimensions are unknown.
     */
    private Dimension  size = null;

    /**
     * The component insets. The inset values are used to create a
     * border around the board to compensate for a skewed aspect
     * ratio. If the component has been resized, the insets values
     * will be recalculated when the paint method executes.
     */
    private Insets  insets = new Insets(0, 0, 0, 0);

    /**
     * The square size in pixels. This value is updated when the
     * component size is changed, i.e. when the <code>size</code>
     * variable is modified.
     */
    private Dimension  squareSize = new Dimension(0, 0);

    /**
     * An image used for double buffering. The board is first
     * painted onto this image, and that image is then painted
     * onto the real surface in order to avoid making the drawing
     * process visible to the user. This image is recreated each
     * time the component size changes.
     */
    private Image  bufferImage = null;

    /**
     * A clip boundary buffer rectangle. This rectangle is used
     * when calculating the clip boundaries, in order to avoid
     * allocating a new clip rectangle for each board square.
     */
    private Rectangle  bufferRect = new Rectangle();

    /**
     * The board message color.
     */
    private Color  messageColor = Color.white;

    /**
     * A lookup table containing lighter versions of the colors.
     * This table is used to avoid calculating the lighter
     * versions of the colors for each and every square drawn.
     */
    private Hashtable lighterColors = new Hashtable();

    /**
     * A lookup table containing darker versions of the colors.
     * This table is used to avoid calculating the darker
     * versions of the colors for each and every square drawn.
     */
    private Hashtable  darkerColors = new Hashtable();

    /**
     * A flag set when the component has been updated.
     */
    private boolean  updated = true;

    /**
     * A bounding box of the squares to update. The coordinates
     * used in the rectangle refers to the square matrix.
     */
    private Rectangle  updateRect = new Rectangle();

    /**
     * Creates a new square board component.
     */
    public GameBoardComponent(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        setBackground(Configuration.getColor("board.background",
                "#000000"));
        messageColor = Configuration.getColor("board.message",
                "#ffffff");
    }

    /**
     * Adds a square to the set of squares in need of redrawing.
     *
     * @param x     the horizontal position (0 <= x < width)
     * @param y     the vertical position (0 <= y < height)
     */
    public void invalidateSquare(int x, int y) {
        if (updated) {
            updated = false;
            updateRect.x = x;
            updateRect.y = y;
            updateRect.width = 0;
            updateRect.height = 0;
        } else {
            if (x < updateRect.x) {
                updateRect.width += updateRect.x - x;
                updateRect.x = x;
            } else if (x > updateRect.x + updateRect.width) {
                updateRect.width = x - updateRect.x;
            }
            if (y < updateRect.y) {
                updateRect.height += updateRect.y - y;
                updateRect.y = y;
            } else if (y > updateRect.y + updateRect.height) {
                updateRect.height = y - updateRect.y;
            }
        }
    }


    /**
     * Redraws all the invalidated squares. If no squares have
     * been marked as in need of redrawing, no redrawing will
     * occur.
     */
    public void redraw() {
        Graphics  g;

        if (!updated) {
            updated = true;
            g = getGraphics();
            g.setClip(insets.left + updateRect.x * squareSize.width,
                    insets.top + updateRect.y * squareSize.height,
                    (updateRect.width + 1) * squareSize.width,
                    (updateRect.height + 1) * squareSize.height);
            paint(g);
        }
    }

    /**
     * Redraws the whole component.
     */
    public void redrawAll() {
        Graphics  g;

        updated = true;
        g = getGraphics();
        g.setClip(insets.left,
                insets.top,
                gameBoard.getWidth() * squareSize.width,
                gameBoard.getHeight() * squareSize.height);
        paint(g);
    }

    /**
     * Returns true as this component is double buffered.
     *
     * @return true as this component is double buffered
     */
    public boolean isDoubleBuffered() {
        return true;
    }

    /**
     * Returns the preferred size of this component.
     *
     * @return the preferred component size
     */
    public Dimension getPreferredSize() {
        return new Dimension(gameBoard.getWidth() * 20, gameBoard.getHeight() * 20);
    }

    /**
     * Returns the minimum size of this component.
     *
     * @return the minimum component size
     */
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    /**
     * Returns the maximum size of this component.
     *
     * @return the maximum component size
     */
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    /**
     * Returns a lighter version of the specified color. The
     * lighter color will looked up in a hashtable, making this
     * method fast. If the color is not found, the ligher color
     * will be calculated and added to the lookup table for later
     * reference.
     *
     * @param c     the base color
     *
     * @return the lighter version of the color
     */
    private Color getLighterColor(Color c) {
        Color  lighter;

        lighter = (Color) lighterColors.get(c);
        if (lighter == null) {
            lighter = c.brighter().brighter();
            lighterColors.put(c, lighter);
        }
        return lighter;
    }

    /**
     * Returns a darker version of the specified color. The
     * darker color will looked up in a hashtable, making this
     * method fast. If the color is not found, the darker color
     * will be calculated and added to the lookup table for later
     * reference.
     *
     * @param c     the base color
     *
     * @return the darker version of the color
     */
    private Color getDarkerColor(Color c) {
        Color  darker;

        darker = (Color) darkerColors.get(c);
        if (darker == null) {
            darker = c.darker().darker();
            darkerColors.put(c, darker);
        }
        return darker;
    }

    /**
     * Paints this component indirectly. The painting is first
     * done to a buffer image, that is then painted directly to
     * the specified graphics context.
     *
     * @param g     the graphics context to use
     */
    public synchronized void paint(Graphics g) {
        Graphics   bufferGraphics;
        Rectangle  rect;

        // Handle component size change
        if (size == null || !size.equals(getSize())) {
            size = getSize();
            squareSize.width = size.width / gameBoard.getWidth();
            squareSize.height = size.height / gameBoard.getHeight();
            if (squareSize.width <= squareSize.height) {
                squareSize.height = squareSize.width;
            } else {
                squareSize.width = squareSize.height;
            }
            insets.left = (size.width - gameBoard.getWidth() * squareSize.width) / 2;
            insets.right = insets.left;
            insets.top = 0;
            insets.bottom = size.height - gameBoard.getHeight() * squareSize.height;
            bufferImage = createImage(gameBoard.getWidth() * squareSize.width,
                    gameBoard.getHeight() * squareSize.height);
        }

        // Paint component in buffer image
        rect = g.getClipBounds();
        bufferGraphics = bufferImage.getGraphics();
        bufferGraphics.setClip(rect.x - insets.left,
                rect.y - insets.top,
                rect.width,
                rect.height);
        paintComponent(bufferGraphics);

        // Paint image buffer
        g.drawImage(bufferImage,
                insets.left,
                insets.top,
                getBackground(),
                null);
    }

    /**
     * Paints this component directly. All the squares on the
     * board will be painted directly to the specified graphics
     * context.
     *
     * @param g     the graphics context to use
     */
    private void paintComponent(Graphics g) {

        // Paint background
        g.setColor(getBackground());
        g.fillRect(0,
                0,
                gameBoard.getWidth() * squareSize.width,
                gameBoard.getHeight() * squareSize.height);

        // Paint squares
        for (int y = 0; y < gameBoard.getHeight(); y++) {
            for (int x = 0; x < gameBoard.getWidth(); x++) {
                if (gameBoard.getMatrix()[y][x] != null) {
                    paintSquare(g, x, y);
                }
            }
        }

        // Paint message
        if (gameBoard.getMessage() != null) {
            paintMessage(g, gameBoard.getMessage());
        }
    }

    /**
     * Paints a single board square. The specified position must
     * contain a color object.
     *
     * @param g     the graphics context to use
     * @param x     the horizontal position (0 <= x < width)
     * @param y     the vertical position (0 <= y < height)
     */
    private void paintSquare(Graphics g, int x, int y) {
        Color  color = gameBoard.getMatrix()[y][x];
        int    xMin = x * squareSize.width;
        int    yMin = y * squareSize.height;
        int    xMax = xMin + squareSize.width - 1;
        int    yMax = yMin + squareSize.height - 1;
        int    i;

        // Skip drawing if not visible
        bufferRect.x = xMin;
        bufferRect.y = yMin;
        bufferRect.width = squareSize.width;
        bufferRect.height = squareSize.height;
        if (!bufferRect.intersects(g.getClipBounds())) {
            return;
        }

        // Fill with base color
        g.setColor(color);
        g.fillRect(xMin, yMin, squareSize.width, squareSize.height);

        // Draw brighter lines
        g.setColor(getLighterColor(color));
        for (i = 0; i < squareSize.width / 10; i++) {
            g.drawLine(xMin + i, yMin + i, xMax - i, yMin + i);
            g.drawLine(xMin + i, yMin + i, xMin + i, yMax - i);
        }

        // Draw darker lines
        g.setColor(getDarkerColor(color));
        for (i = 0; i < squareSize.width / 10; i++) {
            g.drawLine(xMax - i, yMin + i, xMax - i, yMax - i);
            g.drawLine(xMin + i, yMax - i, xMax - i, yMax - i);
        }
    }

    /**
     * Paints a board message. The message will be drawn at the
     * center of the component.
     *
     * @param g     the graphics context to use
     * @param msg   the string message
     */
    private void paintMessage(Graphics g, String msg) {
        int  fontWidth;
        int  offset;
        int  x;
        int  y;

        // Find string font width
        g.setFont(new Font("SansSerif", Font.BOLD, squareSize.width + 4));
        fontWidth = g.getFontMetrics().stringWidth(msg);

        // Find centered position
        x = (gameBoard.getWidth() * squareSize.width - fontWidth) / 2;
        y = gameBoard.getHeight() * squareSize.height / 2;

        // Draw black version of the string
        offset = squareSize.width / 10;
        g.setColor(Color.black);
        g.drawString(msg, x - offset, y - offset);
        g.drawString(msg, x - offset, y);
        g.drawString(msg, x - offset, y - offset);
        g.drawString(msg, x, y - offset);
        g.drawString(msg, x, y + offset);
        g.drawString(msg, x + offset, y - offset);
        g.drawString(msg, x + offset, y);
        g.drawString(msg, x + offset, y + offset);

        // Draw white version of the string
        g.setColor(messageColor);
        g.drawString(msg, x, y);
    }
}
