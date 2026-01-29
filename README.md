# ToDo List

## Tecnologias Utilizadas
- Java 25
- Gradle
- Java Serialization

## Build e Execução
```bash
./gradlew build
./gradlew run --console=plain
```

## Estrutura do Projeto
```
src/
├── Main.java
├── model/    # Entidades do domínio
└── service/  # Lógica de negócio
```

### Modelo de Tarefa
- ID: UUID
- Nome: String
- Descrição: String
- Data de término: LocalDateTime
- Prioridade [1-5]: Enum
- Categoria: String
- Status [TODO, DOING, DONE]: Enum

### Observações
- Ordenação: binary search insertion mantendo a lista ordenada.
- Comparator: `Priority` (decrescente) com desempate por `Deadline` (crescente).
- Persistência: serialização Java nativa com String para LocalDateTime.

Feito por Ângelo.