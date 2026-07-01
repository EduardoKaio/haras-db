package com.haras.security;

import com.haras.model.Pessoa;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * UserDetails do haras. username = email; authorities no formato ROLE_XXX.
 * GERENTE vem da flag is_gerente; os demais papéis vêm das especializações (RoleRepository).
 */
public class PessoaUserDetails implements UserDetails {

    private final int idPessoa;
    private final String email;
    private final String senhaHash;
    private final List<GrantedAuthority> authorities;

    public PessoaUserDetails(Pessoa pessoa, List<String> rolesEspecializacao) {
        this.idPessoa = pessoa.idPessoa();
        this.email = pessoa.email();
        this.senhaHash = pessoa.senha();

        List<GrantedAuthority> auths = new ArrayList<>();
        if (pessoa.gerente()) {
            auths.add(new SimpleGrantedAuthority("ROLE_GERENTE"));
        }
        rolesEspecializacao.forEach(r -> auths.add(new SimpleGrantedAuthority("ROLE_" + r)));
        this.authorities = List.copyOf(auths);
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public List<String> getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senhaHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
