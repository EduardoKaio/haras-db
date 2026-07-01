package com.haras.security;

import com.haras.model.Pessoa;
import com.haras.repository.PessoaRepository;
import com.haras.repository.RoleRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PessoaUserDetailsService implements UserDetailsService {

    private final PessoaRepository pessoaRepository;
    private final RoleRepository roleRepository;

    public PessoaUserDetailsService(PessoaRepository pessoaRepository, RoleRepository roleRepository) {
        this.pessoaRepository = pessoaRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Pessoa pessoa = pessoaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));
        return new PessoaUserDetails(pessoa, roleRepository.findRoles(pessoa.idPessoa()));
    }
}
