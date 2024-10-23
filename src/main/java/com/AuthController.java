/* @RestController
@RequestMapping("/auth")
public class AuthController { 

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Endpoint to handle user login
    @PostMapping("/signin")
    public ResponseEntity<String> login(@RequestBody UserCredentialsDTO credentials) {
        boolean isAuthenticated = authService.authenticate(credentials.getUsername(), credentials.getPassword());

        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // Endpoint to handle user registration
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserCredentialsDTO credentials) {
        boolean isRegistered = authService.registerUser(credentials.getUsername(), credentials.getPassword());

        if (isRegistered) {
            return ResponseEntity.ok("Registration successful");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }
}
 */