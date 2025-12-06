package pe.com.ecocleany.ecosmart.social.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.com.ecocleany.ecosmart.shared.interfaces.rest.SecurityUtils;
import pe.com.ecocleany.ecosmart.social.application.internal.commandservices.PostCommandServiceImpl;
import pe.com.ecocleany.ecosmart.social.application.internal.queryservices.PostQueryServiceImpl;
import pe.com.ecocleany.ecosmart.social.domain.model.commands.CreatePostCommand;
import pe.com.ecocleany.ecosmart.social.domain.model.commands.DeletePostCommand;
import pe.com.ecocleany.ecosmart.social.domain.model.commands.LikePostCommand;
import pe.com.ecocleany.ecosmart.social.domain.model.queries.GetAllPostsQuery;
import pe.com.ecocleany.ecosmart.social.interfaces.rest.resources.CreatePostResource;
import pe.com.ecocleany.ecosmart.social.interfaces.rest.resources.PostResource;
import pe.com.ecocleany.ecosmart.social.interfaces.rest.transform.PostResourceFromEntityAssembler;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/social/posts")
@Tag(name = "Social", description = "Publicaciones, Likes y Tendencias")
public class SocialController {

    private final PostCommandServiceImpl postCommandService;
    private final PostQueryServiceImpl postQueryService;
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    private final PostResourceFromEntityAssembler assembler;

    public SocialController(PostCommandServiceImpl postCommandService,
                            PostQueryServiceImpl postQueryService,
                            SecurityUtils securityUtils,
                            UserRepository userRepository,
                            PostResourceFromEntityAssembler assembler) {
        this.postCommandService = postCommandService;
        this.postQueryService = postQueryService;
        this.securityUtils = securityUtils;
        this.userRepository = userRepository;
        this.assembler = assembler;
    }

    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody CreatePostResource resource) {
        Long userId = 1L;

        try {
            String username = securityUtils.getCurrentUsername();
            if (username != null) {
                var userOpt = userRepository.findByUsername(username);
                if (userOpt.isPresent()) {
                    userId = userOpt.get().getId();
                }
            }
        } catch (Exception ex) {
        }

        var command = new CreatePostCommand(
                resource.content(),
                resource.imageUrl(),
                resource.district(),
                userId
        );
        Long postId = postCommandService.handle(command);
        return new ResponseEntity<>(postId, HttpStatus.CREATED);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        var command = new DeletePostCommand(postId);
        postCommandService.handle(command);
        return ResponseEntity.ok("Publicación eliminada correctamente");
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> likePost(@PathVariable Long postId) {
        var command = new LikePostCommand(postId);
        return postCommandService.handle(command)
                .map(id -> ResponseEntity.ok("Like agregado."))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PostResource>> getAllPosts(
            @RequestParam(required = false) String district,
            @RequestParam(required = false, defaultValue = "false") boolean popular) {

        var query = new GetAllPostsQuery(district, popular);
        var posts = postQueryService.handle(query);

        var resources = posts.stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }
}
