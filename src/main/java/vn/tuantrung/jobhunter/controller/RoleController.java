package vn.tuantrung.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.tuantrung.jobhunter.domain.Role;
import vn.tuantrung.jobhunter.domain.response.ResultPaginationDTO;
import vn.tuantrung.jobhunter.service.RoleService;
import vn.tuantrung.jobhunter.util.annotation.ApiMessage;
import vn.tuantrung.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create a new role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws IdInvalidException {
        // check name
        if (this.roleService.existsByName(role.getName())) {
            throw new IdInvalidException("Role with name = " + role.getName() + " already exists");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.createRole(role));
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> updateRole(@RequestBody Role r) throws IdInvalidException {
         // check id
         if (this.roleService.fetchById(r.getId()) == null) {
            throw new IdInvalidException("Role với id = " + r.getId() + " không tồn tại");
        }

        // // check name
        // if (this.roleService.existsByName(r.getName())) {
        //     throw new IdInvalidException("Role với name = " + r.getName() + " đã tồn tại");
        // }

        return ResponseEntity.ok().body(this.roleService.updateRole(r));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete a role by id")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") long id) throws IdInvalidException{
        //check id
        if(this.roleService.fetchById(id) == null) {
            throw new IdInvalidException("Role with id = " + id + " does not exist");
        }

        this.roleService.deleteRole(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/roles")
    @ApiMessage("Get all roles")
    public ResponseEntity<ResultPaginationDTO> getRoles(@Filter Specification<Role> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.roleService.getRoles(spec,pageable));
    }
    
    @GetMapping("/roles/{id}")
    @ApiMessage("Get a role by id")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") long id) throws IdInvalidException {
        Role role = this.roleService.fetchById(id);
        if(role == null) {
            throw new IdInvalidException("Role with id = " + id + " does not exist");
        }
        return ResponseEntity.ok().body(role);
    }
    
}
