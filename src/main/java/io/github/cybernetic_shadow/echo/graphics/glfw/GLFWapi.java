package io.github.cybernetic_shadow.echo.graphics.glfw;

import static org.lwjgl.system.Checks.checkBuffer;
import static org.lwjgl.system.MemoryUtil.memAddressSafe;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWcharfun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWcharmodsfun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWcursorenterfun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWcursorposfun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWdropfun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWerrorfun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWframebuffersizefun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWkeyfun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWmonitorfun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWmousebuttonfun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWscrollfun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWwindowclosefun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWwindowfocusfun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWwindowiconifyfun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWwindowposfun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWwindowrefreshfun;
import io.github.cybernetic_shadow.echo.graphics.glfw.callback.GLFWwindowsizefun;

/**
 * A wrapper around the GLFW 3 library built on top of LWJGL 3's bindings. This wrapper contains convenience methods as
 * well as different types that LWJGL does not provide.
 * 
 * @author Josh "ShadowLordALpha
 */
public final class GLFWapi {

	private static boolean init = false;
	private static String version = null;
	private static GLFWerrorfun errorfun = null;
	private static GLFWmonitor[] monitors = null;
	private static GLFWmonitor primary = null;

	/**
	 * Private constructor to prevent instancing of this static class
	 */
	private GLFWapi() {
	}

	/*
	 * GLFW API tokens
	 */

	// TODO redo some documentation on keys
	/**
	 * This is incremented when the API is changed in non-compatible ways.
	 */
	public static final int GLFW_VERSION_MAJOR = GLFW.GLFW_VERSION_MAJOR;

	/**
	 * This is incremented when features are added to the API but it remains backward-compatible.
	 */
	public static final int GLFW_VERSION_MINOR = GLFW.GLFW_VERSION_MINOR;

	/**
	 * This is incremented when a bug fix release is made that does not contain any API changes.
	 */
	public static final int GLFW_VERSION_REVISION = GLFW.GLFW_VERSION_REVISION;

	/** The key or button was pressed. */
	public static final int GLFW_PRESS = 0x1;

	/** The key was held down until it repeated. */
	public static final int GLFW_REPEAT = 0x2;

	/** The unknown key. */
	public static final int GLFW_KEY_UNKNOWN = 0xFFFFFFFF;

	/** Printable keys. */
	public static final int GLFW_KEY_SPACE = 0x20, GLFW_KEY_APOSTROPHE = 0x27, GLFW_KEY_COMMA = 0x2C,
			GLFW_KEY_MINUS = 0x2D, GLFW_KEY_PERIOD = 0x2E, GLFW_KEY_SLASH = 0x2F, GLFW_KEY_0 = 0x30, GLFW_KEY_1 = 0x31,
			GLFW_KEY_2 = 0x32, GLFW_KEY_3 = 0x33, GLFW_KEY_4 = 0x34, GLFW_KEY_5 = 0x35, GLFW_KEY_6 = 0x36,
			GLFW_KEY_7 = 0x37, GLFW_KEY_8 = 0x38, GLFW_KEY_9 = 0x39, GLFW_KEY_SEMICOLON = 0x3B, GLFW_KEY_EQUAL = 0x3D,
			GLFW_KEY_A = 0x41, GLFW_KEY_B = 0x42, GLFW_KEY_C = 0x43, GLFW_KEY_D = 0x44, GLFW_KEY_E = 0x45,
			GLFW_KEY_F = 0x46, GLFW_KEY_G = 0x47, GLFW_KEY_H = 0x48, GLFW_KEY_I = 0x49, GLFW_KEY_J = 0x4A,
			GLFW_KEY_K = 0x4B, GLFW_KEY_L = 0x4C, GLFW_KEY_M = 0x4D, GLFW_KEY_N = 0x4E, GLFW_KEY_O = 0x4F,
			GLFW_KEY_P = 0x50, GLFW_KEY_Q = 0x51, GLFW_KEY_R = 0x52, GLFW_KEY_S = 0x53, GLFW_KEY_T = 0x54,
			GLFW_KEY_U = 0x55, GLFW_KEY_V = 0x56, GLFW_KEY_W = 0x57, GLFW_KEY_X = 0x58, GLFW_KEY_Y = 0x59,
			GLFW_KEY_Z = 0x5A, GLFW_KEY_LEFT_BRACKET = 0x5B, GLFW_KEY_BACKSLASH = 0x5C, GLFW_KEY_RIGHT_BRACKET = 0x5D,
			GLFW_KEY_GRAVE_ACCENT = 0x60, GLFW_KEY_WORLD_1 = 0xA1, GLFW_KEY_WORLD_2 = 0xA2;

	/** Function keys. */
	public static final int GLFW_KEY_ESCAPE = 0x100, GLFW_KEY_ENTER = 0x101, GLFW_KEY_TAB = 0x102,
			GLFW_KEY_BACKSPACE = 0x103, GLFW_KEY_INSERT = 0x104, GLFW_KEY_DELETE = 0x105, GLFW_KEY_RIGHT = 0x106,
			GLFW_KEY_LEFT = 0x107, GLFW_KEY_DOWN = 0x108, GLFW_KEY_UP = 0x109, GLFW_KEY_PAGE_UP = 0x10A,
			GLFW_KEY_PAGE_DOWN = 0x10B, GLFW_KEY_HOME = 0x10C, GLFW_KEY_END = 0x10D, GLFW_KEY_CAPS_LOCK = 0x118,
			GLFW_KEY_SCROLL_LOCK = 0x119, GLFW_KEY_NUM_LOCK = 0x11A, GLFW_KEY_PRINT_SCREEN = 0x11B,
			GLFW_KEY_PAUSE = 0x11C, GLFW_KEY_F1 = 0x122, GLFW_KEY_F2 = 0x123, GLFW_KEY_F3 = 0x124, GLFW_KEY_F4 = 0x125,
			GLFW_KEY_F5 = 0x126, GLFW_KEY_F6 = 0x127, GLFW_KEY_F7 = 0x128, GLFW_KEY_F8 = 0x129, GLFW_KEY_F9 = 0x12A,
			GLFW_KEY_F10 = 0x12B, GLFW_KEY_F11 = 0x12C, GLFW_KEY_F12 = 0x12D, GLFW_KEY_F13 = 0x12E,
			GLFW_KEY_F14 = 0x12F, GLFW_KEY_F15 = 0x130, GLFW_KEY_F16 = 0x131, GLFW_KEY_F17 = 0x132,
			GLFW_KEY_F18 = 0x133, GLFW_KEY_F19 = 0x134, GLFW_KEY_F20 = 0x135, GLFW_KEY_F21 = 0x136,
			GLFW_KEY_F22 = 0x137, GLFW_KEY_F23 = 0x138, GLFW_KEY_F24 = 0x139, GLFW_KEY_F25 = 0x13A,
			GLFW_KEY_KP_0 = 0x140, GLFW_KEY_KP_1 = 0x141, GLFW_KEY_KP_2 = 0x142, GLFW_KEY_KP_3 = 0x143,
			GLFW_KEY_KP_4 = 0x144, GLFW_KEY_KP_5 = 0x145, GLFW_KEY_KP_6 = 0x146, GLFW_KEY_KP_7 = 0x147,
			GLFW_KEY_KP_8 = 0x148, GLFW_KEY_KP_9 = 0x149, GLFW_KEY_KP_DECIMAL = 0x14A, GLFW_KEY_KP_DIVIDE = 0x14B,
			GLFW_KEY_KP_MULTIPLY = 0x14C, GLFW_KEY_KP_SUBTRACT = 0x14D, GLFW_KEY_KP_ADD = 0x14E,
			GLFW_KEY_KP_ENTER = 0x14F, GLFW_KEY_KP_EQUAL = 0x150, GLFW_KEY_LEFT_SHIFT = 0x154,
			GLFW_KEY_LEFT_CONTROL = 0x155, GLFW_KEY_LEFT_ALT = 0x156, GLFW_KEY_LEFT_SUPER = 0x157,
			GLFW_KEY_RIGHT_SHIFT = 0x158, GLFW_KEY_RIGHT_CONTROL = 0x159, GLFW_KEY_RIGHT_ALT = 0x15A,
			GLFW_KEY_RIGHT_SUPER = 0x15B, GLFW_KEY_MENU = 0x15C, GLFW_KEY_LAST = GLFW_KEY_MENU;

	/** If this bit is set one or more Shift keys were held down. */
	public static final int GLFW_MOD_SHIFT = 0x1;

	/** If this bit is set one or more Control keys were held down. */
	public static final int GLFW_MOD_CONTROL = 0x2;

	/** If this bit is set one or more Alt keys were held down. */
	public static final int GLFW_MOD_ALT = 0x4;

	/** If this bit is set one or more Super keys were held down. */
	public static final int GLFW_MOD_SUPER = 0x8;

	/**
	 * Mouse buttons. See <a href="http://www.glfw.org/docs/latest/input.html#input_mouse_button">mouse button input</a>
	 * for how these are used.
	 */
	public static final int GLFW_MOUSE_BUTTON_1 = 0x0, GLFW_MOUSE_BUTTON_2 = 0x1, GLFW_MOUSE_BUTTON_3 = 0x2,
			GLFW_MOUSE_BUTTON_4 = 0x3, GLFW_MOUSE_BUTTON_5 = 0x4, GLFW_MOUSE_BUTTON_6 = 0x5, GLFW_MOUSE_BUTTON_7 = 0x6,
			GLFW_MOUSE_BUTTON_8 = 0x7, GLFW_MOUSE_BUTTON_LAST = GLFW_MOUSE_BUTTON_8,
			GLFW_MOUSE_BUTTON_LEFT = GLFW_MOUSE_BUTTON_1, GLFW_MOUSE_BUTTON_RIGHT = GLFW_MOUSE_BUTTON_2,
			GLFW_MOUSE_BUTTON_MIDDLE = GLFW_MOUSE_BUTTON_3;

	/**
	 * Joysticks. See <a href="http://www.glfw.org/docs/latest/input.html#joystick">joystick input</a> for how these are
	 * used.
	 */
	public static final int GLFW_JOYSTICK_1 = 0x0, GLFW_JOYSTICK_2 = 0x1, GLFW_JOYSTICK_3 = 0x2, GLFW_JOYSTICK_4 = 0x3,
			GLFW_JOYSTICK_5 = 0x4, GLFW_JOYSTICK_6 = 0x5, GLFW_JOYSTICK_7 = 0x6, GLFW_JOYSTICK_8 = 0x7,
			GLFW_JOYSTICK_9 = 0x8, GLFW_JOYSTICK_10 = 0x9, GLFW_JOYSTICK_11 = 0xA, GLFW_JOYSTICK_12 = 0xB,
			GLFW_JOYSTICK_13 = 0xC, GLFW_JOYSTICK_14 = 0xD, GLFW_JOYSTICK_15 = 0xE, GLFW_JOYSTICK_16 = 0xF,
			GLFW_JOYSTICK_LAST = GLFW_JOYSTICK_16;

	/**
	 * GLFW has not been initialized.
	 * 
	 * <p>
	 * This occurs if a GLFW function was called that may not be called unless the library is initialized.
	 * </p>
	 */
	public static final int GLFW_NOT_INITIALIZED = 0x10001;

	/**
	 * No context is current for this thread.
	 * 
	 * <p>
	 * This occurs if a GLFW function was called that needs and operates on the current OpenGL or OpenGL ES context but
	 * no context is current on the calling thread. One such function is {@link #glfwSwapInterval SwapInterval}.
	 * </p>
	 */
	public static final int GLFW_NO_CURRENT_CONTEXT = 0x10002;

	/**
	 * One of the arguments to the function was an invalid enum value.
	 * 
	 * <p>
	 * One of the arguments to the function was an invalid enum value, for example requesting {@link #GLFW_RED_BITS
	 * RED_BITS} with {@link #glfwGetWindowAttrib GetWindowAttrib}.
	 * </p>
	 */
	public static final int GLFW_INVALID_ENUM = 0x10003;

	/**
	 * One of the arguments to the function was an invalid value.
	 * 
	 * <p>
	 * One of the arguments to the function was an invalid value, for example requesting a non-existent OpenGL or OpenGL
	 * ES version like 2.7.
	 * </p>
	 * 
	 * <p>
	 * Requesting a valid but unavailable OpenGL or OpenGL ES version will instead result in a
	 * {@link #GLFW_VERSION_UNAVAILABLE VERSION_UNAVAILABLE} error.
	 * </p>
	 */
	public static final int GLFW_INVALID_VALUE = 0x10004;

	/**
	 * A memory allocation failed.
	 * 
	 * <p>
	 * A bug in GLFW or the underlying operating system. Report the bug to our
	 * <a href="https://github.com/glfw/glfw/issues">issue tracker</a>.
	 * </p>
	 */
	public static final int GLFW_OUT_OF_MEMORY = 0x10005;

	/**
	 * GLFW could not find support for the requested client API on the system. If emitted by functions other than @ref
	 * glfwCreateWindow, no supported client API was found.
	 * 
	 * <p>
	 * The installed graphics driver does not support the requested client API, or does not support it via the chosen
	 * context creation backend. Below are a few examples:
	 * </p>
	 * 
	 * <p>
	 * Some pre-installed Windows graphics drivers do not support OpenGL. AMD only supports OpenGL ES via EGL, while
	 * Nvidia and Intel only support it via a WGL or GLX extension. OS X does not provide OpenGL ES at all. The Mesa
	 * EGL, OpenGL and OpenGL ES libraries do not interface with the Nvidia binary driver.
	 * </p>
	 */
	public static final int GLFW_API_UNAVAILABLE = 0x10006;

	/**
	 * The requested OpenGL or OpenGL ES version (including any requested context or framebuffer hints) is not available
	 * on this machine.
	 * 
	 * <p>
	 * The machine does not support your requirements. If your application is sufficiently flexible, downgrade your
	 * requirements and try again. Otherwise, inform the user that their machine does not match your requirements.
	 * </p>
	 * 
	 * <p>
	 * Future invalid OpenGL and OpenGL ES versions, for example OpenGL 4.8 if 5.0 comes out before the 4.x series gets
	 * that far, also fail with this error and not {@link #GLFW_INVALID_VALUE INVALID_VALUE}, because GLFW cannot know
	 * what future versions will exist.
	 * </p>
	 */
	public static final int GLFW_VERSION_UNAVAILABLE = 0x10007;

	/**
	 * A platform-specific error occurred that does not match any of the more specific categories.
	 * 
	 * <p>
	 * A bug or configuration error in GLFW, the underlying operating system or its drivers, or a lack of required
	 * resources. Report the issue to our <a href="https://github.com/glfw/glfw/issues">issue tracker</a>.
	 * </p>
	 */
	public static final int GLFW_PLATFORM_ERROR = 0x10008;

	/**
	 * The requested format is not supported or available.
	 * 
	 * <p>
	 * If emitted during window creation, one or more hard constraints did not match any of the available pixel formats.
	 * If your application is sufficiently flexible, downgrade your requirements and try again. Otherwise, inform the
	 * user that their machine does not match your requirements.
	 * </p>
	 * 
	 * <p>
	 * If emitted when querying the clipboard, ignore the error or report it to the user, as appropriate.
	 * </p>
	 */
	public static final int GLFW_FORMAT_UNAVAILABLE = 0x10009;

	/** Window attributes. */
	public static final int GLFW_FOCUSED = 0x20001, GLFW_ICONIFIED = 0x20002, GLFW_RESIZABLE = 0x20003,
			GLFW_VISIBLE = 0x20004, GLFW_DECORATED = 0x20005, GLFW_AUTO_ICONIFY = 0x20006, GLFW_FLOATING = 0x20007;

	/** Input options. */
	public static final int GLFW_CURSOR = 0x33001, GLFW_STICKY_KEYS = 0x33002, GLFW_STICKY_MOUSE_BUTTONS = 0x33003;

	/** Cursor state. */
	public static final int GLFW_CURSOR_NORMAL = 0x34001, GLFW_CURSOR_HIDDEN = 0x34002, GLFW_CURSOR_DISABLED = 0x34003;

	/**
	 * Standard cursor shapes. See <a href="http://www.glfw.org/docs/latest/input.html#cursor_standard">standard cursor
	 * creation</a> for how these are used.
	 */
	public static final int GLFW_ARROW_CURSOR = 0x36001, GLFW_IBEAM_CURSOR = 0x36002, GLFW_CROSSHAIR_CURSOR = 0x36003,
			GLFW_HAND_CURSOR = 0x36004, GLFW_HRESIZE_CURSOR = 0x36005, GLFW_VRESIZE_CURSOR = 0x36006;

	/** Monitor events. */
	public static final int GLFW_CONNECTED = 0x40001, GLFW_DISCONNECTED = 0x40002;

	/** Don't care value. */
	public static final int GLFW_DONT_CARE = 0xFFFFFFFF;

	/** PixelFormat hints. */
	public static final int GLFW_RED_BITS = 0x21001, GLFW_GREEN_BITS = 0x21002, GLFW_BLUE_BITS = 0x21003,
			GLFW_ALPHA_BITS = 0x21004, GLFW_DEPTH_BITS = 0x21005, GLFW_STENCIL_BITS = 0x21006,
			GLFW_ACCUM_RED_BITS = 0x21007, GLFW_ACCUM_GREEN_BITS = 0x21008, GLFW_ACCUM_BLUE_BITS = 0x21009,
			GLFW_ACCUM_ALPHA_BITS = 0x2100A, GLFW_AUX_BUFFERS = 0x2100B, GLFW_STEREO = 0x2100C, GLFW_SAMPLES = 0x2100D,
			GLFW_SRGB_CAPABLE = 0x2100E, GLFW_REFRESH_RATE = 0x2100F, GLFW_DOUBLE_BUFFER = 0x21010;

	/** Client API hints. */
	public static final int GLFW_CLIENT_API = 0x22001, GLFW_CONTEXT_VERSION_MAJOR = 0x22002,
			GLFW_CONTEXT_VERSION_MINOR = 0x22003, GLFW_CONTEXT_REVISION = 0x22004, GLFW_CONTEXT_ROBUSTNESS = 0x22005,
			GLFW_OPENGL_FORWARD_COMPAT = 0x22006, GLFW_OPENGL_DEBUG_CONTEXT = 0x22007, GLFW_OPENGL_PROFILE = 0x22008,
			GLFW_CONTEXT_RELEASE_BEHAVIOR = 0x22009;

	/** Values for the {@link #GLFW_CLIENT_API CLIENT_API} hint. */
	public static final int GLFW_OPENGL_API = 0x30001, GLFW_OPENGL_ES_API = 0x30002;

	/** Values for the {@link #GLFW_CONTEXT_ROBUSTNESS CONTEXT_ROBUSTNESS} hint. */
	public static final int GLFW_NO_ROBUSTNESS = 0x0, GLFW_NO_RESET_NOTIFICATION = 0x31001,
			GLFW_LOSE_CONTEXT_ON_RESET = 0x31002;

	/** Values for the {@link #GLFW_OPENGL_PROFILE OPENGL_PROFILE} hint. */
	public static final int GLFW_OPENGL_ANY_PROFILE = 0x0, GLFW_OPENGL_CORE_PROFILE = 0x32001,
			GLFW_OPENGL_COMPAT_PROFILE = 0x32002;

	/** Values for the {@link #GLFW_CONTEXT_RELEASE_BEHAVIOR CONTEXT_RELEASE_BEHAVIOR} hint. */
	public static final int GLFW_ANY_RELEASE_BEHAVIOR = 0x0, GLFW_RELEASE_BEHAVIOR_FLUSH = 0x35001,
			GLFW_RELEASE_BEHAVIOR_NONE = 0x35002;

	/*
	 * GLFW API functions
	 */

	/**
	 * Initializes the GLFW library.
	 * <p>
	 * This function initializes the GLFW library. Before most GLFW functions can be used, GLFW must be initialized, and
	 * before an application terminates GLFW should be terminated in order to free any resources allocated during or
	 * after initialization.
	 * <p>
	 * If this function fails, it calls {@link #glfwTerminate} before returning. If it succeeds, you should call
	 * {@link #glfwTerminate} before the application exits.
	 * <p>
	 * Additional calls to this function after successful initialization but before termination will return {@code true}
	 * immediately.
	 * 
	 * @return {@code true} if successful or {@code false} in an
	 * <a href="http://www.glfw.org/docs/latest/intro.html#error_handling">error</a> occurred.
	 * 
	 * @sla.remarks <b>OS X:</b> This function will change the current directory of the application to the
	 * {@code Contents/Resources} subdirectory of the application's bundle, if present. This can be disabled with a
	 * <a href="http://www.glfw.org/docs/latest/compile.html#compile_options_osx">compile-time option</a>.
	 * 
	 * @sla.remarks <b>X11:</b> If the {@code LC_CTYPE} category of the current locale is set to {@code "C"} then the
	 * environment's locale will be applied to that category. This is done because character input will not function
	 * when {@code LC_CTYPE} is set to {@code "C"}. If another locale was set before this function was called, it will
	 * be left untouched.
	 * 
	 * @sla.ts This function may only be called from the main thread.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/intro.html#intro_init">Initialization and termination</a>
	 * 
	 * @see #glfwTerminate
	 * 
	 * @since Added in GLFW 1.0
	 */
	public static boolean glfwInit() {
		if (!init) { // only run if false
			if (GLFW.glfwInit() == GL11.GL_TRUE) { // if true set init to true
				init = true;
			} else { // else return false
				return false;
			}
		}
		return init;
	}

	/**
	 * Terminates the GLFW library.
	 * <p>
	 * This function destroys all remaining windows and cursors, restores any modified gamma ramps and frees any other
	 * allocated resources. Once this function is called, you must again call {@link #glfwInit} successfully before you
	 * will be able to use most GLFW functions.
	 * <p>
	 * If GLFW has been successfully initialized, this function should be called before the application exits. If
	 * initialization fails, there is no need to call this function, as it is called by {@link #glfwInit} before it
	 * returns failure.
	 * 
	 * @sla.remarks This function may be called before {@link #glfwInit}.
	 * 
	 * @sla.warn No window's context may be current on another thread when this function is called.
	 * 
	 * @sla.reen This function may not be called from a callback.
	 * 
	 * @sla.ts This function may only be called from the main thread.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/intro.html#intro_init">Initialization and termination</a>
	 * 
	 * @see #glfwInit
	 * 
	 * @since Added in GLFW 1.0
	 */
	public static void glfwTerminate() {
		if (init) { // If GLFW has been initialized otherwise this method does nothing
			GLFW.glfwTerminate();
			init = false;
		}
	}

	/**
	 * Retrieves the version of the GLFW library.
	 * <p>
	 * This function retrieves the major, minor and revision numbers of the GLFW library. It is intended for when you
	 * are using GLFW as a shared library and want to ensure that you are using the minimum required version.
	 * <p>
	 * Any or all of the version arguments may be {@code null}. This function always succeeds.
	 * 
	 * @param major Where to store the major version number, or {@code null}.
	 * 
	 * @param minor Where to store the minor version number, or {@code null}.
	 * 
	 * @param rev Where to store the revision number, or {@code null}.
	 * 
	 * @sla.remarks This function may be called before {@link #glfwInit}.
	 * 
	 * @sla.ts This function may be called from any thread.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/intro.html#intro_version">Version management</a>
	 * 
	 * @see #glfwGetVersionString
	 * 
	 * @since Added in GLFW 1.0
	 */
	public static void glfwGetVersion(ByteBuffer major, ByteBuffer minor, ByteBuffer rev) {
		if (major != null) {
			if (LWJGLUtil.CHECKS) {
				checkBuffer(major, 4);
			}
			major.putInt(major.position(), GLFWapi.GLFW_VERSION_MAJOR);
		}
		if (minor != null) {
			if (LWJGLUtil.CHECKS) {
				checkBuffer(minor, 4);
			}
			minor.putInt(minor.position(), GLFWapi.GLFW_VERSION_MINOR);
		}
		if (rev != null) {
			if (LWJGLUtil.CHECKS) {
				checkBuffer(rev, 4);
			}
			rev.putInt(rev.position(), GLFWapi.GLFW_VERSION_REVISION);
		}
	}

	/**
	 * Alternative version of: {@link #glfwGetVersion(ByteBuffer, ByteBuffer, ByteBuffer) glfwGetVersion}.
	 * 
	 * @param major Where to store the major version number, or {@code null}.
	 * 
	 * @param minor Where to store the minor version number, or {@code null}.
	 * 
	 * @param rev Where to store the revision version number, or {@code null}.
	 */
	public static void glfwGetVersion(IntBuffer major, IntBuffer minor, IntBuffer rev) {
		if (major != null) {
			if (LWJGLUtil.CHECKS) {
				checkBuffer(major, 1);
			}
			major.put(major.position(), GLFWapi.GLFW_VERSION_MAJOR);
		}
		if (minor != null) {
			if (LWJGLUtil.CHECKS) {
				checkBuffer(minor, 1);
			}
			minor.put(minor.position(), GLFWapi.GLFW_VERSION_MINOR);
		}
		if (rev != null) {
			if (LWJGLUtil.CHECKS) {
				checkBuffer(rev, 1);
			}
			rev.put(rev.position(), GLFWapi.GLFW_VERSION_REVISION);
		}
	}

	/**
	 * Alternative version of: {@link #glfwGetVersion(ByteBuffer, ByteBuffer, ByteBuffer) glfwGetVersion}.
	 * <p>
	 * This version takes one argument to store all version data and returns that one argument.
	 * 
	 * @param dest Where to store the major, minor, and revision data.
	 * 
	 * @return The buffer that the version data was stored in.
	 * 
	 * @sla.remarks The version argument must not be {@code null} and able to hold all three values otherwise it is the
	 * same as {@link #glfwGetVersion(ByteBuffer, ByteBuffer, ByteBuffer) glfwGetVersion}.
	 */
	public static ByteBuffer glfwGetVersion(ByteBuffer dest) {
		if (LWJGLUtil.CHECKS) {
			checkBuffer(dest, 12);
		}
		dest.putInt(dest.position(), GLFWapi.GLFW_VERSION_MAJOR);
		dest.putInt(dest.position() + 4, GLFWapi.GLFW_VERSION_MINOR);
		dest.putInt(dest.position() + 8, GLFWapi.GLFW_VERSION_REVISION);
		return dest;
	}

	/**
	 * Alternative version of: {@link #glfwGetVersion(ByteBuffer) glfwGetVersion}
	 * 
	 * @param dest Where to store the major, minor, and revision data.
	 * 
	 * @return The buffer that the version data was stored in.
	 */
	public static IntBuffer glfwGetVerision(IntBuffer dest) {
		if (LWJGLUtil.CHECKS) {
			checkBuffer(dest, 3);
		}
		dest.put(dest.position(), GLFWapi.GLFW_VERSION_MAJOR);
		dest.put(dest.position() + 1, GLFWapi.GLFW_VERSION_MINOR);
		dest.put(dest.position() + 2, GLFWapi.GLFW_VERSION_REVISION);
		return dest;
	}

	/**
	 * Returns a string describing the compile-time configuration.
	 * <p>
	 * This function returns the compile-time generated
	 * <a href="http://www.glfw.org/docs/latest/intro.html#intro_version_string">version string</a> of the GLFW library
	 * binary. It describes the version, platform, compiler and any platform-specific compile-time options.
	 * <p>
	 * <b>Do not use the version string</b> to parse the GLFW library version. The
	 * {@link #glfwGetVersion(ByteBuffer, ByteBuffer, ByteBuffer) glfwGetVersion} function already provides the version
	 * of the running library binary.
	 * <p>
	 * This function always succeeds.
	 * 
	 * @return The GLFW version string.
	 * 
	 * @sla.remarks This function may be called before {@link #glfwInit}
	 * 
	 * @sla.remarks The returned string is static and compile-time generated.
	 * 
	 * @sla.ts This function may be called from any thread.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/intro.html#intro_version">Version management</a>
	 * 
	 * @see #glfwGetVersion(ByteBuffer, ByteBuffer, ByteBuffer) glfwGetVersion
	 * 
	 * @since Added in GLFW 3.0
	 */
	public static String glfwGetVersionString() {
		if (version == null) {
			version = GLFW.glfwGetVersionString();
		}
		return version;
	}

	/**
	 * Sets the error callback.
	 * <p>
	 * This function sets the error callback, which is called with an error code and a human-readable description each
	 * time a GLFW error occurs.
	 * <p>
	 * The error callback is called on the thread where the error occurred. If you are using GLFW from multiple threads,
	 * your error callback needs to be written accordingly.
	 * <p>
	 * Because the description string may have been generated specifically for that error, it is not guaranteed to be
	 * valid after the callback has returned. If you wish to use it after the callback returns, you need to make a copy.
	 * <p>
	 * Once set, the error callback remains set even after the library has been terminated.
	 * 
	 * @param cbfun The new callback, or {@code null} to remove the currently set callback.
	 * 
	 * @return The previously set callback, or {@code null} if no callback was set.
	 * 
	 * @sla.remarks This function may be called before {@link #glfwInit}.
	 * 
	 * @sla.ts This function may only be called from the main thread.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/intro.html#error_handling">Error handling</a>
	 * 
	 * @since Added in GLFW 3.0
	 */
	public static GLFWerrorfun glfwSetErrorCallback(GLFWerrorfun cbfun) {
		GLFWerrorfun errorfun = GLFWapi.errorfun;
		GLFWapi.errorfun = cbfun;
		GLFWErrorCallback temp = GLFW.glfwSetErrorCallback(new GLFWErrorCallback() {

			@Override
			public void invoke(int error, long description) {
				cbfun.callback(error, GLFWErrorCallback.getDescription(description));
			}
		});
		if (temp != null) { // release old callback
			temp.release();
		}
		return errorfun;
	}

	/**
	 * Returns the currently connected monitors.
	 * <p>
	 * This function returns an array of handles for all currently connected monitors.
	 * 
	 * @param count Where to store the number of monitors in the returned array, or {@code null}. This is set to zero if
	 * an error occurred.
	 * 
	 * @return An array of monitor handles, or {@code null} if an
	 * <a href="http://www.glfw.org/docs/latest/intro.html#error_handling">error</a> occurred.
	 * 
	 * @sla.remarks The returned array is only guaranteed to be valid until the monitor configuration changes or the
	 * library is terminated.
	 * 
	 * @sla.ts This function may only be called from the main thread.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/monitor.html#monitor_monitors">Retrieving monitors</a>
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/monitor.html#monitor_event">Monitor configuration changes</a>
	 * 
	 * @see #glfwGetPrimaryMonitor
	 * 
	 * @since Added in GLFW 3.0
	 */
	public static GLFWmonitor[] glfwGetMonitors(ByteBuffer count) {
		// This array should never be more than 10 elements so checking should be fast
		PointerBuffer monitors = GLFW.glfwGetMonitors();
		int cou = monitors.remaining();
		if (count != null) {
			if (LWJGLUtil.CHECKS) {
				checkBuffer(count, 4);
			}
			count.putInt(count.position(), cou);
		}
		if (cou == 0) {
			return null;
		}
		boolean dirty = false;
		GLFWmonitor[] monitorarray = new GLFWmonitor[cou];
		for (int i = 0; i < cou; i++) {
			GLFWmonitor madd = null;
			for (GLFWmonitor test : GLFWapi.monitors) {
				if (test.address() == monitors.get(i)) {
					madd = test;
					break;
				}
			}
			if (madd == null) {
				if (primary != null && primary.address() == monitors.get(i)) {
					madd = primary;
				} else {
					madd = new GLFWmonitor(monitors.get(i));
				}
				dirty = true;
			}
			monitorarray[i] = madd;
		}
		if (dirty) {
			GLFWapi.monitors = monitorarray;
		}
		return GLFWapi.monitors;
	}

	/**
	 * Returns the primary monitor.
	 * <p>
	 * This function returns the primary monitor. This is usually the monitor where elements like the Windows task bar
	 * or the OS X menu bar are located.
	 * 
	 * @return The primary monitor, or {@code null} if an
	 * <a href="http://www.glfw.org/docs/latest/intro.html#error_handling">error</a> occurred.
	 * 
	 * @sla.ts This function may only be called from the main thread.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/monitor.html#monitor_monitors">Retrieving monitors</a>
	 * 
	 * @see #glfwGetMonitors
	 */
	public static GLFWmonitor glfwGetPrimaryMonitor() {
		long pointer = GLFW.glfwGetPrimaryMonitor();
		if (pointer == MemoryUtil.NULL) {
			return null;
		}
		if (primary != null && primary.address() == pointer) {
			return primary;
		}
		for (GLFWmonitor test : monitors) {
			if (test.address() == pointer) {
				primary = test;
				return primary;
			}
		}
		primary = new GLFWmonitor(pointer);
		return primary;
	}

	/**
	 * Returns the position of the monitor's viewport on the virtual screen.
	 * <p>
	 * This function returns the position, in screen coordinates, of the upper-left corner of the specified monitor.
	 * <p>
	 * Any or all of the position arguments may be {@code null}. If an
	 * <a href="http://www.glfw.org/docs/latest/intro.html#error_handling">error</a> occurs, all non-{@code null}
	 * position arguments will be set to zero.
	 * 
	 * @param monitor The monitor to query.
	 * 
	 * @param xpos Where to store the monitor x-coordinate, or {@code null}.
	 * 
	 * @param ypos Where to store the monitor y-coordinate, or {@code null}.
	 * 
	 * @sla.ts This function may only be called from the main thread.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/monitor.html#monitor_properties">Monitor properties</a>
	 * 
	 * @since Added in GLFW 3.0
	 */
	public static void glfwGetMonitorPos(GLFWmonitor monitor, ByteBuffer xpos, ByteBuffer ypos) {
		GLFW.glfwGetMonitorPos(monitor.address(), xpos, ypos);
	}

	/**
	 * Alternate version of: {@link #glfwGetMonitorPos(GLFWmonitor, ByteBuffer, ByteBuffer) glfwGetMonitorPos}.
	 * 
	 * @param monitor The monitor to query.
	 * 
	 * @param xpos Where to store the monitor x-coordinate, or {@code null}.
	 * 
	 * @param ypos Where to store the monitor y-coordinate, or {@code null}.
	 */
	public static void glfwGetMonitorPos(GLFWmonitor monitor, IntBuffer xpos, IntBuffer ypos) {
		GLFW.glfwGetMonitorPos(monitor.address(), xpos, ypos);
	}

	/**
	 * Alternate version of {@link #glfwGetMonitorPos(GLFWmonitor, ByteBuffer, ByteBuffer) glfwGetMonitorPos}.
	 * <p>
	 * This version takes one argument to store all position data and returns that one argument.
	 * 
	 * @param monitor The monitor to query.
	 * 
	 * @param dest Where to store the position data.
	 * 
	 * @return The buffer that the position data was stored in.
	 */
	public static ByteBuffer glfwGetMonitorPos(GLFWmonitor monitor, ByteBuffer dest) {
		if (LWJGLUtil.CHECKS) {
			if (dest != null) {
				checkBuffer(dest, 8);
			}
		}
		GLFW.nglfwGetMonitorPos(monitor.address(), memAddressSafe(dest), memAddressSafe(dest) + 4);
		return dest;
	}

	/**
	 * Alternate version of {@link #glfwGetMonitorPos(GLFWmonitor, ByteBuffer) glfwGetMonitorPos}.
	 * 
	 * @param monitor The monitor to query.
	 * 
	 * @param dest Where to store the position data.
	 * 
	 * @return The buffer that the position data was stored in.
	 */
	public static IntBuffer glfwGetMonitorPos(GLFWmonitor monitor, IntBuffer dest) {
		if (LWJGLUtil.CHECKS) {
			if (dest != null) {
				checkBuffer(dest, 2);
			}
		}
		GLFW.nglfwGetMonitorPos(monitor.address(), memAddressSafe(dest), memAddressSafe(dest) + 1);
		return dest;
	}

	// TODO
	public static void glfwGetMonitorPhysicalSize (GLFWmonitor monitor, ByteBuffer widthMM, ByteBuffer heightMM) {
		
	}

	public static String glfwGetMonitorName(GLFWmonitor monitor) {
		return GLFW.glfwGetMonitorName(monitor.address());
	}

	public static GLFWmonitorfun glfwSetMonitorCallback(GLFWmonitorfun cbfun) {
		return null;
	}

	public static GLFWvidmode glfwGetVideoModes(GLFWmonitor monitor, ByteBuffer count) {
		return null;
	}

	public static GLFWvidmode glfwGetVideoMode(GLFWmonitor monitor) {
		return null;
	}

	public static void glfwSetGamma(GLFWmonitor monitor, float gamma) {
		
	}

	public static GLFWgammaramp glfwGetGammaRamp(GLFWmonitor monitor) {
		return null;
	}

	public static void glfwSetGammaRamp(GLFWmonitor monitor, GLFWgammaramp ramp) {

	}

	/**
	 * Resets all window hints to their default values.
	 * <p>
	 * This function resets all window hints to their
	 * <a href="http://www.glfw.org/docs/latest/window.html#window_hints_values">default values</a>.
	 * 
	 * @sla.ts This function may only be called from the main thread.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/window.html#window_hints">Window creation hints</a>
	 * 
	 * @see #glfwWindowHint(int, int) glfwWindowHint
	 * 
	 * @since Added in GLFW 3.0
	 */
	public static void glfwDefaultWindowHints() {
		GLFW.glfwDefaultWindowHints();
	}

	/**
	 * Sets the specified window hint to the desired value.
	 * <p>
	 * This function sets hints for the next call to {@link #glfwCreateWindow}. The hints, once set, retain their values
	 * until changed by a call to {@link #glfwWindowHint(int, int) glfwWindowHint} or {@link #glfwDefaultWindowHints},
	 * or until the library is terminated.
	 * 
	 * @param target The <a href="http://www.glfw.org/docs/latest/window.html#window_hints">window hint</a> to set.
	 * 
	 * @param hint The new value of the window hint.
	 * 
	 * @sla.ts This function may only be called from the main thread.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/window.html#window_hints">Window creation hints</a>
	 * 
	 * @see {@link #glfwDefaultWindowHints}
	 * 
	 * @since Added in GLFW 3.0. Replaces {@code glfwOpenWindowHint}.
	 */
	public static void glfwWindowHint(int target, int hint) {
		GLFW.glfwWindowHint(target, hint);
	}

	/**
	 * Alternate version of: {@link #glfwWindowHint(int, int) glfwWindowHint}.
	 * 
	 * @param target The <a href="http://www.glfw.org/docs/latest/window.html#window_hints">window hint</a> to set.
	 * 
	 * @param hint The new value of the window hint.
	 */
	public static void glfwWindowHint(int target, boolean hint) {
		glfwWindowHint(target, (hint) ? GL11.GL_TRUE : GL11.GL_FALSE);
	}

	/**
	 * Creates a window and its associated context.
	 * <p>
	 * This function creates a window and its associated OpenGL or OpenGL ES context. Most of the options controlling
	 * how the window and its context should be created are specified with
	 * <a href="http://www.glfw.org/docs/latest/window.html#window_hints">window hints</a>.
	 * <p>
	 * Successful creation does not change which context is current. Before you can use the newly created context, you
	 * need to <a href="http://www.glfw.org/docs/latest/context.html#context_current">make it current</a>. For
	 * information about the {@code share} parameter, see
	 * <a href="http://www.glfw.org/docs/latest/context.html#context_sharing">Context object sharing</a>.
	 * <p>
	 * The created window, framebuffer and context may differ from what you requested, as not all parameters and hints
	 * are <a href="http://www.glfw.org/docs/latest/window.html#window_hints_hard">hard constraints</a>. This includes
	 * the size of the window, especially for full screen windows. To query the actual attributes of the created window,
	 * framebuffer and context, see {@link #glfwGetWindowAttrib}, {@link #glfwGetWindowSize} and
	 * {@link glfwGetFramebufferSize}.
	 * <p>
	 * To create a full screen window, you need to specify the monitor the window will cover. If no monitor is
	 * specified, windowed mode will be used. Unless you have a way for the user to choose a specific monitor, it is
	 * recommended that you pick the primary monitor. For more information on how to query connected monitors, see
	 * <a href="http://www.glfw.org/docs/latest/monitor.html#monitor_monitors">Retrieving monitors</a>.
	 * <p>
	 * For full screen windows, the specified size becomes the resolution of the window's <i>desired video mode</i>. As
	 * long as a full screen window has input focus, the supported video mode most closely matching the desired video
	 * mode is set for the specified monitor. For more information about full screen windows, including the creation of
	 * so called <i>windowed full screen</i> or <i>borderless full screen</i> windows, see
	 * <a href="http://www.glfw.org/docs/latest/window.html#window_windowed_full_screen">"Windowed full screen"
	 * windows</a>.
	 * <p>
	 * By default, newly created windows use the placement recommended by the window system. To create the window at a
	 * specific position, make it initially invisible using the {@link #GLFW_VISIBLE} window hint, set its
	 * <a href="http://www.glfw.org/docs/latest/window.html#window_pos">position</a> and then
	 * <a href="http://www.glfw.org/docs/latest/window.html#window_hide">show</a> it.
	 * <p>
	 * If a full screen window has input focus, the screensaver is prohibited from starting.
	 * <p>
	 * Window systems put limits on window sizes. Very large or very small window dimensions may be overridden by the
	 * window system on creation. Check the actual
	 * <a href="http://www.glfw.org/docs/latest/window.html#window_size">size</a> after creation.
	 * <p>
	 * The <a href="http://www.glfw.org/docs/latest/window.html#buffer_swap">swap interval</a> is not set during window
	 * creation and the initial value may vary depending on driver settings and defaults.
	 * 
	 * @param width The desired width, in screen coordinates, of the window. This must be greater than zero.
	 * 
	 * @param height The desired height, in screen coordinates, of the window. This must be greater than zero.
	 * 
	 * @param title The initial, UTF-8 encoded window title.
	 * 
	 * @param monitor The monitor to use for full screen mode, or {@code null} to use windowed mode.
	 * 
	 * @param share The window whose context to share resources with, or {@code null} to not share resources.
	 * 
	 * @return The handle of the created window, or {@code null} if an
	 * <a href="http://www.glfw.org/docs/latest/intro.html#error_handling">error</a> occurred.
	 * 
	 * @sla.remarks <b>Windows:</b> Window creation will fail if the Microsoft GDI software OpenGL implementation is the
	 * only one available.
	 * 
	 * @sla.remarks <b>Windows:</b> If the executable has an icon resource named {@code GLFW_ICON}, it will be set as
	 * the icon for the window. If no such icon is present, the {@code IDI_WINLOGO} icon will be used instead.
	 * 
	 * @sla.remarks <b>Windows:</b> The context to share resources with may not be current on any other thread.
	 * 
	 * @sla.remarks <b>OS X:</b> The GLFW window has no icon, as it is not a document window, but the dock icon will be
	 * the same as the application bundle's icon. For more information on bundles, see the
	 * <a href="https://developer.apple.com/library/mac/documentation/CoreFoundation/Conceptual/CFBundles/">Bundle
	 * Programming Guide</a> in the Mac Developer Library.
	 * 
	 * @sla.remarks <b>OS X:</b> The first time a window is created the menu bar is populated with common commands like
	 * Hide, Quit and About. The About entry opens a minimal about dialog with information from the application's
	 * bundle. The menu bar can be disabled with a
	 * <a href="http://www.glfw.org/docs/latest/compile.html#compile_options_osx">compile-time option</a>.
	 * 
	 * @sla.remarks <b>OS X:</b> On OS X 10.10 and later the window frame will not be rendered at full resolution on
	 * Retina displays unless the {@code NSHighResolutionCapable} key is enabled in the application bundle's
	 * {@code Info.plist}. For more information, see <a href=
	 * "https://developer.apple.com/library/mac/documentation/GraphicsAnimation/Conceptual/HighResolutionOSX/Explained/Explained.html">
	 * High Resolution Guidelines for OS X</a> in the Mac Developer Library.
	 * 
	 * @sla.remarks <b>X11:</b> There is no mechanism for setting the window icon yet.
	 * 
	 * @sla.remarks <b>X11:</b> Some window managers will not respect the placement of initially hidden windows.
	 * 
	 * @sla.reen This function may not be called from a callback.
	 * 
	 * @sla.ts This function may only be called from the main thread.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/window.html#window_creation">Window creation</a>
	 * 
	 * @see #glfwDestroyWindow
	 * 
	 * @since Added in GLFW 3.0. Replaces {@code glfwOpenWindow}.
	 */
	public static GLFWwindow glfwCreateWindow(int width, int height, String title, GLFWmonitor monitor,
			GLFWwindow share) {
		long temp = GLFW.glfwCreateWindow(width, height, title, (monitor == null) ? MemoryUtil.NULL : monitor.address(),
				(share == null) ? MemoryUtil.NULL : share.address());
		if (temp == 0) {
			return null;
		} else {
			return new GLFWwindow(temp);
		}
	}

	/**
	 * Destroys the specified window and its context.
	 * <p>
	 * This function destroys the specified window and its context. On calling this function, no further callbacks will
	 * be called for that window.
	 * <p>
	 * If the context of the specified window is current on the main thread, it is detached before being destroyed.
	 * 
	 * @param window The window to destroy.
	 * 
	 * @sla.remarks The context of the specified window must not be current on any other thread when this function is
	 * called.
	 * 
	 * @sla.reen This function may not be called from a callback.
	 * 
	 * @sla.ts This function may only be called from the main thread.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/window.html#window_creation">Window creation</a>
	 * 
	 * @see #glfwCreateWindow
	 * 
	 * @since Added in GLFW 3.0. Replaces {@code glfwCloseWindow}.
	 */
	public static void glfwDestroyWindow(GLFWwindow window) {
		GLFW.glfwDestroyWindow(window.address());
	}

	/**
	 * Checks the close flag of the specified window.
	 * <p>
	 * This function returns the value of the close flag of the specified window.
	 * 
	 * @param window The window to query.
	 * 
	 * @return The value of the close flag.
	 * 
	 * @sla.ts This function may be called from any thread. Access is not synchronized.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/window.html#window_close">Window closing and close flap</a>
	 * 
	 * @since Added in GLFW 3.0
	 */
	public static boolean glfwWindowShouldClose(GLFWwindow window) {
		return GLFW.glfwWindowShouldClose(window.address()) == GL11.GL_TRUE;
	}

	/**
	 * Sets the close flag of the specified window.
	 * <p>
	 * This function sets the value of the close flag of the specified window. This can be used to override the user's
	 * attempt to close the window, or to signal that it should be closed.
	 * 
	 * @param window The window whose flag to change.
	 * 
	 * @param value The new value.
	 * 
	 * @sla.ts This function may be called from any thread. Access is not synchronized.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/window.html#window_close">Window closing and close flap</a>
	 * 
	 * @since Added in GLFW 3.0
	 */
	public static void glfwSetWindowShouldClose(GLFWwindow window, boolean value) {
		GLFW.glfwSetWindowShouldClose(window.address(), (value) ? GL11.GL_TRUE : GL11.GL_FALSE);
	}

	/**
	 * Sets the title of the specified window.
	 * <p>
	 * This function sets the window title, encoded as UTF-8, of the specified window.
	 * 
	 * @param window The window whose title to change.
	 * 
	 * @param title The UTF-8 encoded window title.
	 * 
	 * @sla.remarks <b>OS X:</b> The window title will not be updated until the next time you process events.
	 * 
	 * @sla.ts This function may only be called from the main thread.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/window.html#window_title">Window title</a>
	 * 
	 * @since Added in GLFW 1.0. GLFW 3.0 added window handle parameter.
	 */
	public static void glfwSetWindowTitle(GLFWwindow window, String title) {
		GLFW.glfwSetWindowTitle(window.address(), title);
	}

	// TODO all alternate versions of methods after this point if they so need them

	/**
	 * Retrieves the position of the client area of the specified window.
	 * <p>
	 * This function retrieves the position, in screen coordinates, of the upper-left corner of the client area of the
	 * specified window.
	 * <p>
	 * Any or all of the position arguments may be {@code null}. If an error occurs, all non-{@code null} position
	 * arguments will be set to zero.
	 * 
	 * @param window The window to query.
	 * 
	 * @param Where to store the x-coordinate of the upper-left corner of the client area, or {@code null}.
	 * 
	 * @param Where to store the y-coordinate of the upper-left corner of the client area, or {@code null}.
	 * 
	 * @sla.ts This function may only be called from the main thread.
	 * 
	 * @see <a href="http://www.glfw.org/docs/latest/window.html#window_pos">Window position</a>
	 * 
	 * @see #glfwSetWindowPos
	 * 
	 * @since Added in GLFW 3.0
	 */
	public static void glfwGetWindowPos(GLFWwindow window, ByteBuffer xpos, ByteBuffer ypos) {
		GLFW.glfwGetWindowPos(window.address(), xpos, ypos);
	}

	// TODO unless a method is documented it is considered incomplete and should not be expected to work

	public static void glfwSetWindowPos(GLFWwindow window, int xpos, int ypos) {
		GLFW.glfwSetWindowPos(window.address(), xpos, ypos);
	}

	public static void glfwGetWindowSize(GLFWwindow window, ByteBuffer width, ByteBuffer height) {
		GLFW.glfwGetWindowSize(window.address(), width, height);
	}

	public static void glfwSetWindowSize(GLFWwindow window, ByteBuffer width, ByteBuffer height) {

	}

	public static void glfwGetFramebufferSize(GLFWwindow window, ByteBuffer width, ByteBuffer height) {

	}

	public static void glfwGetWindowFrameSize(GLFWwindow window, ByteBuffer left, ByteBuffer top, ByteBuffer right,
			ByteBuffer bottom) {

	}

	public static void glfwIconifyWindow(GLFWwindow window) {

	}

	public static void glfwRestoreWindow(GLFWwindow window) {

	}

	public static void glfwShowWindow(GLFWwindow window) {
		GLFW.glfwShowWindow(window.address());
	}

	public static void glfwHideWindow(GLFWwindow window) {
		GLFW.glfwHideWindow(window.address());
	}

	public static GLFWmonitor glfwGetWindowMonitor(GLFWwindow window) {
		return null;
	}

	public static int glfwGetWindowAttrib(GLFWwindow window, int attrib) {
		return 0;
	}

	public static void glfwSetWindowUserPointer(GLFWwindow window, long pointer) {

	}

	public static void glfwGetWindowUserPointer(GLFWwindow window) {

	}

	public static GLFWwindowposfun glfwSetWindowPosCallback(GLFWwindow window, GLFWwindowposfun cbfun) {
		return null;
	}

	public static GLFWwindowsizefun glfwSetWindowSizeCallback(GLFWwindow window, GLFWwindowsizefun cbfun) {
		return null;
	}

	public static GLFWwindowclosefun glfwSetWindowCloseCallback(GLFWwindow window, GLFWwindowclosefun cbfun) {
		return null;
	}

	public static GLFWwindowrefreshfun glfwSetWindowRefreshCallback(GLFWwindow window, GLFWwindowrefreshfun cbfun) {
		return null;
	}

	public static GLFWwindowfocusfun glfwSetWindowFocusCallback(GLFWwindow window, GLFWwindowfocusfun cbfun) {
		return null;
	}

	public static GLFWwindowiconifyfun glfwSetWindowIconifyCallback(GLFWwindow window, GLFWwindowiconifyfun cbfun) {
		return null;
	}

	public static GLFWframebuffersizefun glfwSetFramebufferSizeCallback(GLFWwindow window,
			GLFWframebuffersizefun cbfun) {
		return null;
	}

	public static void glfwPollEvents() {
		GLFW.glfwPollEvents();
	}

	public static void glfwWaitEvents() {
		GLFW.glfwWaitEvents();
	}

	public static void glfwPostEmptyEvent() {
		GLFW.glfwPostEmptyEvent();
	}

	public static int glfwGetInputMode(GLFWwindow window, int mode) {
		return 0;
	}

	public static void glfwSetInputMode(GLFWwindow window, int mode, int value) {

	}

	public static int glfwGetKey(GLFWwindow window, int key) {
		return GLFW.glfwGetKey(window.address(), key);
	}

	public static int glfwGetMouseButton(GLFWwindow window, int button) {
		return 0;
	}

	public static void glfwGetCursorPos(GLFWwindow window, ByteBuffer xpos, ByteBuffer ypos) {
		GLFW.glfwGetCursorPos(window.address(), xpos, ypos);
	}

	public static void glfwSetCursorPos(GLFWwindow window, double xpos, double ypos) {
		GLFW.glfwSetCursorPos(window.address(), xpos, ypos);
	}

	public static GLFWcursor glfwCreateCursor(GLFWimage image, int xhot, int yhot) {
		return null;
	}

	public static GLFWcursor glfwCreateStandardCursor(int shape) {
		return null;
	}

	public static void glfwDestroyCursor(GLFWcursor cursor) {

	}

	public static void glfwSetCursor(GLFWwindow window, GLFWcursor cursor) {

	}

	public static GLFWkeyfun glfwSetKeyCallback(GLFWwindow window, GLFWkeyfun cbfun) {
		return null;
	}

	public static GLFWcharfun glfwSetCharCallback(GLFWwindow window, GLFWcharfun cbfun) {
		return null;
	}

	public static GLFWcharmodsfun glfwSetCharModsCallback(GLFWwindow window, GLFWcharmodsfun cbfun) {
		return null;
	}

	public static GLFWmousebuttonfun glfwSetMouseButtonCallback(GLFWwindow window, GLFWmousebuttonfun cbfun) {
		return null;
	}

	public static GLFWcursorposfun glfwSetCursorPosCallback(GLFWwindow window, GLFWcursorposfun cbfun) {
		return null;
	}

	public static GLFWcursorenterfun glfwSetCursorEnterCallback(GLFWwindow window, GLFWcursorenterfun cbfun) {
		return null;
	}

	public static GLFWscrollfun glfwSetScrollCallback(GLFWwindow window, GLFWscrollfun cbfun) {
		return null;
	}

	public static GLFWdropfun glfwSetDropCallback(GLFWwindow window, GLFWdropfun cbfun) {
		return null;
	}

	public static int glfwJoystickPresent(int joy) {
		return 0;
	}

	public static float glfwGetJoystickAxes(int joy, ByteBuffer count) {
		return 0;
	}

	public static ByteBuffer glfwGetJoystickButtons(int joy, ByteBuffer count) {
		ByteBuffer temp = GLFW.glfwGetJoystickButtons(joy);
		// TODO put number in count
		return temp;
	}

	public static String glfwGetJoystickName(int joy) {
		return GLFW.glfwGetJoystickName(joy);
	}

	public static void glfwSetClipboardString(GLFWwindow window, String string) {
		GLFW.glfwSetClipboardString(window.address(), string);
	}

	public static String glfwGetClipboardString(GLFWwindow window) {
		return GLFW.glfwGetClipboardString(window.address());
	}

	public static double glfwGetTime() {
		return GLFW.glfwGetTime();
	}

	public static void glfwSetTime(double time) {
		GLFW.glfwSetTime(time);
	}

	public static void glfwMakeContextCurrent(GLFWwindow window) {
		GLFW.glfwMakeContextCurrent(window.address());
	}

	public static GLFWwindow glfwGetCurrentContext() {
		long window = GLFW.glfwGetCurrentContext();
		if (window == MemoryUtil.NULL) {
			return null;
		}

		// TODO

		return null;
	}

	public static void glfwSwapBuffers(GLFWwindow window) {
		GLFW.glfwSwapBuffers(window.address());
	}

	public static void glfwSwapInterval(int interval) {
		GLFW.glfwSwapInterval(interval);
	}

	public static boolean glfwExtensionSupported(String extension) {
		return GLFW.glfwExtensionSupported(extension) == GL11.GL_TRUE;
	}

	/*
	 * TODO public static GLFWglproc glfwGetProcAddress (const char *procname) {
	 * 
	 * }
	 */

}