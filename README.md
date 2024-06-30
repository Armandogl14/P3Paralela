# P3 por Armando Gonzalez y Moises Rodriguez

## Requisitos de Programación Concurrente
Además de implementar las topologías de redes, se incorporó programación concurrente en las implementaciones utilizando herramientas de concurrencia en Java, tales como:

- **ExecutorService**: Gestionar la ejecución concurrente de los nodos.
- **Thread Safety**: Asegurar operaciones críticas seguras para hilos utilizando mecanismos como `synchronized`, `Locks` o `Concurrent Collections`.
- **Simulación Concurrente**: Los nodos pueden enviar y recibir mensajes concurrentemente.

### Clase `NetworkTopology`
Interfaz base que define los métodos comunes para todas las topologías de redes.

### Subclases para cada Topología de Red
Implementaciones específicas de cada topología de red:

`BusNetwork`
La clase BusNetwork implementa la interfaz NetworkTopology y simula una topología de red en bus. Aquí te explico cómo funciona:

Características de la Topología en Bus

En una topología en bus, todos los nodos están conectados a un único canal de comunicación. Esto significa que todos los mensajes enviados a través del bus pueden ser recibidos por todos los nodos, aunque normalmente solo el nodo destinatario procesa el mensaje.

Implementación de BusNetwork

Atributos:

nodes: Un ConcurrentHashMap que mapea los IDs de los nodos a los objetos Node.
executor: Un ExecutorService para manejar la concurrencia, permitiendo la ejecución de tareas en múltiples hilos.
lock: Un ReentrantLock para asegurar la exclusión mutua al modificar la estructura de datos compartida.

Constructor:

Inicializa el ConcurrentHashMap para almacenar los nodos.
Inicializa el ReentrantLock para controlar el acceso concurrente.
Inicializa el ExecutorService con un tamaño de pool de hilos igual al número de nodos.

Método setupTopology:

Crea e inserta los nodos en el ConcurrentHashMap.
Utiliza el ReentrantLock para asegurar que la configuración de la topología no sea interrumpida por otros hilos.

Método sendMessage:

Encola una tarea en el ExecutorService para enviar un mensaje desde un nodo a otro.
Utiliza el ReentrantLock para asegurar que solo un hilo pueda acceder al nodo destino a la vez.
Llama al método recieveMessage del nodo destino para entregarle el mensaje.

Método receiveMessage:

Similar a sendMessage, utiliza el ReentrantLock para asegurar que solo un hilo pueda acceder al nodo receptor a la vez.
Entrega el mensaje al nodo receptor llamando a su método recieveMessage.

    public class BusNetwork implements NetworkTopology{
        private ConcurrentHashMap<Integer, Node> nodes;
        private ExecutorService executor;
        private ReentrantLock lock;

        public BusNetwork(int numberOfNodes){
            nodes = new ConcurrentHashMap<>();
            lock = new ReentrantLock();
            executor = Executors.newFixedThreadPool(numberOfNodes);
        }

        @Override
        public void setupTopology(int numberOfNodes) {
            lock.lock();
            try {
                for (int i = 0; i < numberOfNodes; i++) {
                    nodes.put(i, new Node(i));
                }
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void sendMessage(int fromNodeId, int toNodeId, Message message) {
            executor.execute(() -> {
                lock.lock();
                try {
                    nodes.get(toNodeId).recieveMessage(message);
                } finally {
                    lock.unlock();
                }
            });
        }

        @Override
        public void receiveMessage(int nodeId, Message message) {
            lock.lock();
            try {
                nodes.get(nodeId).recieveMessage(message);
            } finally {
                lock.unlock();
            }
        }
    }

`RingNetwork`
La clase RingNetwork implementa la interfaz NetworkTopology y simula una topología de red en anillo. Aquí te explico cómo funciona:

Características de la Topología en Anillo

En una topología en anillo, cada nodo está conectado exactamente a otros dos nodos, formando un anillo cerrado. Los mensajes son enviados a través del anillo en una dirección (normalmente unidireccional), pasando por cada nodo hasta llegar al destinatario.

Implementación de RingNetwork

Atributos:

nodes: Un ConcurrentHashMap que mapea los IDs de los nodos a los objetos Node.
executor: Un ExecutorService para manejar la concurrencia, permitiendo la ejecución de tareas en múltiples hilos.
lock: Un ReentrantLock para asegurar la exclusión mutua al modificar la estructura de datos compartida.

Constructor:

Inicializa el ConcurrentHashMap para almacenar los nodos.
Inicializa el ReentrantLock para controlar el acceso concurrente.
Inicializa el ExecutorService con un tamaño de pool de hilos igual al número de nodos.

Método setupTopology:

Crea e inserta los nodos en el ConcurrentHashMap.
Utiliza el ReentrantLock para asegurar que la configuración de la topología no sea interrumpida por otros hilos.

Método sendMessage:

Encola una tarea en el ExecutorService para enviar un mensaje desde un nodo a otro.
Utiliza el ReentrantLock para asegurar que solo un hilo pueda acceder al nodo destino a la vez.
Calcula el ID del siguiente nodo en el anillo (fromNodeId + 1) % nodes.size() y llama al método recieveMessage del nodo siguiente para entregarle el mensaje.

Método receiveMessage:

Similar a sendMessage, utiliza el ReentrantLock para asegurar que solo un hilo pueda acceder al nodo receptor a la vez.
Entrega el mensaje al nodo receptor llamando a su método recieveMessage.

    public class RingNetwork implements NetworkTopology{
        private ConcurrentHashMap<Integer, Node> nodes;
        private ExecutorService executor;
        private ReentrantLock lock;

        public RingNetwork(int numberOfNodes){
            nodes = new ConcurrentHashMap<>();
            lock = new ReentrantLock();
            executor = Executors.newFixedThreadPool(numberOfNodes);
        }
        @Override
        public void setupTopology(int numberOfNodes) {
            lock.lock();
            try {
                for (int i = 0; i < numberOfNodes; i++) {
                    nodes.put(i, new Node(i));
                }
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void sendMessage(int fromNodeId, int toNodeId, Message message) {
            executor.execute(() -> {
                lock.lock();
                try {
                    int nextNodeId = (fromNodeId + 1) % nodes.size();
                    nodes.get(nextNodeId).recieveMessage(message);
                } finally {
                    lock.unlock();
                }
            });
        }

        @Override
        public void receiveMessage(int nodeId, Message message) {
            lock.lock();
            try {
                nodes.get(nodeId).recieveMessage(message);
            } finally {
                lock.unlock();
            }
        }

`MeshNetwork`
La clase MeshNetwork implementa la interfaz NetworkTopology y simula una topología de red en malla. Aquí te explico cómo funciona:

Características de la Topología en Malla

En una topología en malla, cada nodo está conectado a muchos otros nodos (potencialmente a todos los demás nodos). Esta topología proporciona redundancia y alta disponibilidad, ya que hay múltiples rutas posibles para que los mensajes lleguen a su destino.

Implementación de MeshNetwork

Atributos:

nodes: Un ConcurrentHashMap que mapea los IDs de los nodos a los objetos Node.
executor: Un ExecutorService para manejar la concurrencia, permitiendo la ejecución de tareas en múltiples hilos.
lock: Un ReentrantLock para asegurar la exclusión mutua al modificar la estructura de datos compartida.

Constructor:

Inicializa el ConcurrentHashMap para almacenar los nodos.
Inicializa el ReentrantLock para controlar el acceso concurrente.
Inicializa el ExecutorService con un tamaño de pool de hilos igual al número de nodos.

Método setupTopology:

Crea e inserta los nodos en el ConcurrentHashMap.
Utiliza el ReentrantLock para asegurar que la configuración de la topología no sea interrumpida por otros hilos.

Método sendMessage:

Encola una tarea en el ExecutorService para enviar un mensaje desde un nodo a otro.
Utiliza el ReentrantLock para asegurar que solo un hilo pueda acceder al nodo destino a la vez.
Llama al método recieveMessage del nodo destino para entregarle el mensaje.

Método receiveMessage:

Similar a sendMessage, utiliza el ReentrantLock para asegurar que solo un hilo pueda acceder al nodo receptor a la vez.
Entrega el mensaje al nodo receptor llamando a su método recieveMessage.

Método broadcastMessage:

Implementa el método por defecto de la interfaz NetworkTopology para enviar un mensaje a todos los nodos de la red.
Utiliza un bucle para enviar el mensaje a todos los nodos excepto al nodo de origen (fromNodeId).
    
    public class MeshNetwork implements NetworkTopology{
        private ConcurrentHashMap<Integer, Node> nodes;
        private ExecutorService executor;
        private ReentrantLock lock;

        public MeshNetwork(int numberOfNodes){
            nodes = new ConcurrentHashMap<>();
            lock = new ReentrantLock();
            executor = Executors.newFixedThreadPool(numberOfNodes);
        }
        
        @Override
        public void setupTopology(int numberOfNodes) {
            lock.lock();
            try {
                for (int i = 0; i < numberOfNodes; i++) {
                    nodes.put(i, new Node(i));
                }
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void sendMessage(int fromNodeId, int toNodeId, Message message) {
            executor.execute(() -> {
                lock.lock();
                try {
                    nodes.get(toNodeId).recieveMessage(message);
                } finally {
                    lock.unlock();
                }
            });
        }

        @Override
        public void receiveMessage(int nodeId, Message message) {
            lock.lock();
            try {
                nodes.get(nodeId).recieveMessage(message);
            } finally {
            lock.unlock();
            }
        }

        public void broadcastMessage(int fromNodeId, Message message) {
            for (int nodeId : nodes.keySet()) {
                if (nodeId != fromNodeId) {
                    sendMessage(fromNodeId, nodeId, message);
                }
            }
        }
    }
`StarNetwork`
La clase StarNetwork implementa la interfaz NetworkTopology y simula una topología de red en estrella. Aquí te explico cómo funciona:

Características de la Topología en Estrella

En una topología en estrella, todos los nodos están conectados a un nodo central. Este nodo central actúa como un intermediario para todas las comunicaciones entre los nodos.

Implementación de StarNetwork

Atributos:

nodes: Un ConcurrentHashMap que mapea los IDs de los nodos a los objetos Node.
executor: Un ExecutorService para manejar la concurrencia, permitiendo la ejecución de tareas en múltiples hilos.
lock: Un ReentrantLock para asegurar la exclusión mutua al modificar la estructura de datos compartida.
center: El nodo central al que están conectados todos los demás nodos.

Constructor:

Inicializa el ConcurrentHashMap para almacenar los nodos.
Inicializa el ReentrantLock para controlar el acceso concurrente.
Inicializa el ExecutorService con un tamaño de pool de hilos igual al número de nodos.

Método setupTopology:

Crea e inserta el nodo central (center) y los demás nodos en el ConcurrentHashMap.
Utiliza el ReentrantLock para asegurar que la configuración de la topología no sea interrumpida por otros hilos.

Método sendMessage:

Encola una tarea en el ExecutorService para enviar un mensaje.
Utiliza el ReentrantLock para asegurar que solo un hilo pueda acceder al nodo central o al nodo destino a la vez.
Si el nodo de origen es el nodo central (fromNodeId == 0), envía el mensaje directamente al nodo destino.
Si el nodo de origen no es el nodo central, envía el mensaje al nodo central, que luego lo reenvía al nodo destino.

Método receiveMessage:

Similar a sendMessage, utiliza el ReentrantLock para asegurar que solo un hilo pueda acceder al nodo central o al nodo receptor a la vez.
Si el nodo receptor es el nodo central (nodeId == 0), el mensaje es recibido por el nodo central.
Si el nodo receptor no es el nodo central, el mensaje es recibido directamente por el nodo receptor.

    public class StarNetwork implements NetworkTopology {
        private ConcurrentHashMap<Integer, Node> nodes;
        private ExecutorService executor;
        private ReentrantLock lock;
        private Node center;

        public StarNetwork(int numberOfNodes){
            nodes = new ConcurrentHashMap<>();
            lock = new ReentrantLock();
            executor = Executors.newFixedThreadPool(numberOfNodes);
        }

        @Override
        public void setupTopology(int numberOfNodes) {
            lock.lock();
            try {
                center = new Node(0);
                for (int i = 1; i < numberOfNodes; i++) {
                    nodes.put(i, new Node(i));
                }
            } finally {
            lock.unlock();
            }
        }

        @Override
        public void sendMessage(int fromNodeId, int toNodeId, Message message) {
            executor.execute(() -> {
                lock.lock();
                try {
                    if (fromNodeId == 0) {
                        nodes.get(toNodeId).recieveMessage(message);
                    } else {
                        center.recieveMessage(message);
                    }
                } finally {
                    lock.unlock();
                }
            });
        }

        @Override
        public void receiveMessage(int nodeId, Message message) {
            lock.lock();
            try {
                if (nodeId == 0) {
                    center.recieveMessage(message);
                } else {
                    nodes.get(nodeId).recieveMessage(message);
                }
            } finally {
                lock.unlock();
            }
        }
    }

`HypercubeNetwork`

    public void setupTopology(int numberOfNodes) {
            this.n = (int) (Math.log(numberOfNodes) / Math.log(2));
            this.adjMatrix = new int[numberOfNodes][numberOfNodes];
            for (int i = 0; i < numberOfNodes; i++) {
                for (int j = 0; j < numberOfNodes; j++) {
                    adjMatrix[i][j] = 0;
                }
            }
            for (int i = 0; i < numberOfNodes; i++) {
                for (int j = 0; j < n; j++) {
                    int neighbour = i ^ (1 << j);
                    adjMatrix[i][neighbour] = 1;
                }
            }
        }
   
        @Override
        public void sendMessage(int fromNodeId, int toNodeId, Message message) {
            executorService.submit(() -> {
                if (adjMatrix[fromNodeId][toNodeId] == 1) {
                    System.out.println("Message sent from node " + fromNodeId + " to node " + toNodeId);
                    receiveMessage(toNodeId, message);
                } else {
                    System.out.println("Nodes are not directly connected. Message cannot be sent.");
                }
            });
        }
    
        @Override
        public void receiveMessage(int nodeId, Message message) {
            System.out.println("Node " + nodeId + " received message: " + message.getContent());
        }
    
        @Override
        public void broadcastMessage(int fromNodeId, Message message) {
            for (int i = 0; i < adjMatrix[fromNodeId].length; i++) {
                if (adjMatrix[fromNodeId][i] == 1) {
                    sendMessage(fromNodeId, i, message);
                }
            }
        }
    
        public void shutdown() {
            executorService.shutdown();
        }

La clase `HypercubeNetwork`, que implementa una red hipercubo usando una matriz de adyacencia para representar las conexiones entre nodos. Cada nodo está conectado a otros nodos a través de sus vecinos en el hipercubo. La clase incluye métodos para configurar la topología de la red, enviar mensajes entre nodos (usando un `ExecutorService` con un pool de hilos de tamaño fijo para manejar la concurrencia), recibir mensajes y difundir mensajes a todos los nodos vecinos. El método `shutdown` cierra el `ExecutorService` de manera ordenada cuando ya no se necesita.
`TreeNetwork`
    
     public void setupTopology(int numberOfNodes) {
          this.numberOfNodes = numberOfNodes;
          this.adjList = new ArrayList<>(numberOfNodes);
  
          for (int i = 0; i < numberOfNodes; i++) {
              adjList.add(new ArrayList<>());
          }
  
          // En una red de árbol, cada nodo está conectado a un nodo padre y a cero o más nodos hijos.
          // Aquí, simplemente conectamos cada nodo con su nodo padre (el nodo con índice i está conectado con el nodo con índice i/2).
          for (int i = 1; i < numberOfNodes; i++) {
              int parentNodeId = (i - 1) / 2; // Cambiado a (i - 1) / 2 para obtener correctamente el padre
              adjList.get(parentNodeId).add(i);
              adjList.get(i).add(parentNodeId);
          }
          System.out.println("Configurando topología de red en árbol con " + numberOfNodes + " nodos.");
      }
      @Override
    public void sendMessage(int fromNodeId, int toNodeId, Message message) {
        executorService.submit(() -> {
            if (adjList.get(fromNodeId).contains(toNodeId)) {
                System.out.println("Enviando mensaje desde nodo " + fromNodeId + " a nodo " + toNodeId + ": " + message.getContent());
                receiveMessage(toNodeId, message);
            } else {
                System.out.println("Los nodos no están directamente conectados. No se puede enviar el mensaje.");
            }
        });
    }

    @Override
    public void receiveMessage(int nodeId, Message message) {
        System.out.println("Recibiendo mensaje en nodo " + nodeId + ": " + message.getContent());
    }

    @Override
    public void broadcastMessage(int fromNodeId, Message message) {
        for (int i : adjList.get(fromNodeId)) {
            sendMessage(fromNodeId, i, message);
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
La clase TreeNetwork, que representa una red en forma de árbol. La red se implementa utilizando una lista de listas (adjList) para almacenar las conexiones entre nodos, y un ExecutorService con un pool de hilos de tamaño fijo (4 hilos) para manejar la concurrencia. La topología de la red se configura en el método setupTopology, donde cada nodo se conecta a su nodo padre según un esquema de árbol binario. Los métodos incluyen sendMessage para enviar mensajes entre nodos de manera concurrente, receiveMessage para recibir mensajes, y broadcastMessage para difundir mensajes a todos los nodos hijos. El método shutdown cierra el ExecutorService de manera ordenada cuando ya no se necesita.
`FullyConnectedNetwork`
    
    public void setupTopology(int numberOfNodes) {
            this.numberOfNodes = numberOfNodes;
            this.adjList = new ArrayList<>(numberOfNodes);
    
            for (int i = 0; i < numberOfNodes; i++) {
                adjList.add(new ArrayList<>());
            }
    
            // En una red completamente conectada, cada nodo está conectado a todos los demás nodos.
            for (int i = 0; i < numberOfNodes; i++) {
                for (int j = 0; j < numberOfNodes; j++) {
                    if (i != j) {
                        adjList.get(i).add(j);
                    }
                }
            }
            System.out.println("Configurando topología de red completamente conectada con " + numberOfNodes + " nodos.");
        }
        public void sendMessage(int fromNodeId, int toNodeId, Message message) {
        executorService.submit(() -> {
            if (adjList.get(fromNodeId).contains(toNodeId)) {
                System.out.println("Enviando mensaje desde nodo " + fromNodeId + " a nodo " + toNodeId + ": " + message.getContent());
                receiveMessage(toNodeId, message);
            } else {
                System.out.println("Los nodos no están directamente conectados. No se puede enviar el mensaje.");
            }
        });
    }

    @Override
    public void receiveMessage(int nodeId, Message message) {
        System.out.println("Recibiendo mensaje en nodo " + nodeId + ": " + message.getContent());
    }

    @Override
    public void broadcastMessage(int fromNodeId, Message message) {
        for (int i = 0; i < numberOfNodes; i++) {
            if (i != fromNodeId) {
                sendMessage(fromNodeId, i, message);
            }
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
La clase `FullyConnectedNetwork`, que representa una red completamente conectada donde cada nodo está conectado a todos los demás nodos. Usa una lista de listas (`adjList`) para almacenar las conexiones y un `ExecutorService` con un pool de hilos de tamaño fijo (4 hilos) para manejar las tareas concurrentes. La clase incluye métodos para configurar la topología de la red (`setupTopology`), enviar mensajes entre nodos de forma concurrente (`sendMessage`), recibir mensajes (`receiveMessage`), y difundir mensajes a todos los nodos (`broadcastMessage`). También proporciona un método (`shutdown`) para cerrar el `ExecutorService` de manera ordenada cuando ya no se necesite.
`SwitchedNetwork`

    public void setupTopology(int numberOfNodes) {
            this.numberOfNodes = numberOfNodes;
            this.adjList = new ArrayList<>(numberOfNodes);
    
            for (int i = 0; i < numberOfNodes; i++) {
                adjList.add(new ArrayList<>());
            }
    
            // En una red conmutada, cada nodo está conectado a un conmutador.
            // Vamos a suponer que tenemos un conmutador central al cual todos los nodos se conectan.
            // Representamos el conmutador central con el nodo 0.
            for (int i = 1; i < numberOfNodes; i++) {
                adjList.get(0).add(i);  // Conectar el nodo 0 (conmutador) con cada nodo
                adjList.get(i).add(0);  // Conectar cada nodo con el nodo 0 (conmutador)
            }
            System.out.println("Configurando topología de red conmutada con " + numberOfNodes + " nodos.");
    
            // Imprimir la lista de adyacencia para depuración
            printAdjList();
        }
        @Override
    public void sendMessage(int fromNodeId, int toNodeId, Message message) {
        executorService.submit(() -> {
            // Verificar si los nodos están correctamente conectados al conmutador central (nodo 0)
            if ((fromNodeId == 0 || adjList.get(fromNodeId).contains(0)) &&
                    (toNodeId == 0 || adjList.get(toNodeId).contains(0))) {
                System.out.println("Enviando mensaje desde nodo " + fromNodeId + " a nodo " + toNodeId + " a través del conmutador central: " + message.getContent());
                receiveMessage(toNodeId, message);
            } else {
                System.out.println("Los nodos no están conectados al conmutador central. No se puede enviar el mensaje.");
            }
        });
    }

    @Override
    public void receiveMessage(int nodeId, Message message) {
        System.out.println("Recibiendo mensaje en nodo " + nodeId + ": " + message.getContent());
    }

    @Override
    public void broadcastMessage(int fromNodeId, Message message) {
        for (int i = 1; i < numberOfNodes; i++) {
            if (i != fromNodeId) {
                sendMessage(fromNodeId, i, message);
            }
        }
    }

    // Método para imprimir la lista de adyacencia (para depuración)
    private void printAdjList() {
        for (int i = 0; i < adjList.size(); i++) {
            System.out.println("Nodo " + i + " está conectado a: " + adjList.get(i));
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
La clase SwitchedNetwork, que modela una red conmutada donde todos los nodos están conectados a un conmutador central (representado por el nodo 0). La clase utiliza una lista de listas (adjList) para almacenar las conexiones y un ExecutorService con un pool de hilos de tamaño fijo (4 hilos) para manejar la concurrencia. El método setupTopology configura la topología de la red conectando cada nodo al conmutador central. Los métodos incluyen sendMessage para enviar mensajes entre nodos a través del conmutador central de manera concurrente, receiveMessage para recibir mensajes, y broadcastMessage para difundir mensajes a todos los nodos conectados. Además, proporciona un método shutdown para cerrar el ExecutorService de manera ordenada, y un método privado printAdjList para imprimir la lista de adyacencia con fines de depuración.

### Clase `Node`
Representa un nodo en la red y contiene atributos como ID, vecinos, etc.

### Clase `Message`
Representa un mensaje que se envía entre nodos en la red.

### Clase `NetworkManager`
Gestiona la creación y ejecución de la red, así como la comunicación entre nodos.

### Clase `Main`
Punto de entrada principal del programa, donde se configuran y ejecutan las topologías de redes.

---

En este proyecto se ejecutaron todas las topologias al mismo tiempo viendo los resultados de los mensajes enviados:

Para probrar el framework simplemente hay que crear una instancian de framework manager y colocar en el contructor la topología de red que se desea utilizar y luego llamar al método setup network, tanto al constructor como al método se le pasa la cantidad de nodos que tendrá la red. Aquí hay un ejemplo ejemplo:

    NetworkManager busManager = new NetworkManager(new BusNetwork(5));
    busManager.setupNetwork(5);

Para enviar un mensaje simplemente creamos una instancia de mensaje con el contenido del mensaje, y luego llamamos al método del manage sendMessage donde le pasamos el nodo que envía el mensaje, el que lo recibe y el mensaje.

    Message busMessage = new Message("(BusNetwork): Hola nodo 2", 1, 2);
    busManager.sendMessage(1, 2, busMessage);

Aquí tenemos ejemplo de salida para todas las topologías:

![Captura del codigo](https://github.com/Armandogl14/P3Paralela/blob/main/Salida_practica_3.png)




