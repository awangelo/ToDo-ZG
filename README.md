# ToDo List

## Tecnologias Utilizadas
- Java 25
- Gradle
- Java Serialization
- ScheduledExecutorService (para alarme)

## Build e Execução
```bash
./gradlew build
./gradlew test -i
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
- Alarme habilitado: boolean
- Alarme disparado: boolean

Ao criar uma tarefa, o usuário pode escolher se deseja habilitar o alarme (S/N).

Thread em background verifica alarmes a cada 1 minuto.

O tempo de antecedência do alarme é definido automaticamente com base na prioridade da tarefa.

### Observações
- Ordenação: binary search insertion mantendo a lista ordenada.
- Comparator: `Priority` (decrescente) com desempate por `Deadline` (crescente).
- Persistência: serialização Java nativa com String para LocalDateTime.

Feito por Ângelo.